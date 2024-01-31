package kr.project.backend.service.admin;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import kr.project.backend.common.CommonErrorCode;
import kr.project.backend.common.CommonException;
import kr.project.backend.common.Constants;
import kr.project.backend.dto.admin.response.*;
import kr.project.backend.dto.common.request.PushRequestDto;
import kr.project.backend.dto.common.request.PushsRequestDto;
import kr.project.backend.entity.user.UseClause;
import kr.project.backend.entity.user.User;
import kr.project.backend.entity.user.UserUseClause;
import kr.project.backend.repository.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static kr.project.backend.common.PushContent.makeMessage;
import static kr.project.backend.common.PushContent.makeMessages;
import static kr.project.backend.utils.AesUtil.encryptAES256;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    @Value("${admin.aesKey}")
    private String adminAESKey;

    @Value("${admin.aesIv}")
    private String adminAESIv;

    private final UserRepository userRepository;
    private final DropUserRepository dropUserRepository;
    private final MoveViewRepository moveViewRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final UseClauseRepository useClauseRepository;
    private final UserUseClauseRepository userUseClauseRepository;


    public AccessKeyResponseDto giveApikey(String plainText) throws Exception {
        return new AccessKeyResponseDto(encryptAES256(adminAESKey, adminAESIv, plainText + System.currentTimeMillis()));
    }

    @Transactional(readOnly = true)
    public TodayRegisterResponseDto getTodayRegister() {
        int todayRegister = userRepository.countByCreatedDateBetween(
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new TodayRegisterResponseDto(todayRegister);
    }

    @Transactional(readOnly = true)
    public TodayLoginUserResponseDto getTodayLoginUser() {
        int todayLoginUser = userRepository.countByUserLoginDttmBetween(
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new TodayLoginUserResponseDto(todayLoginUser);
    }

    @Transactional(readOnly = true)
    public TodayDropUserResponseDto getTodayDropUser() {
        int todayDropUser = dropUserRepository.countByDropDttmBetween(
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new TodayDropUserResponseDto(todayDropUser);
    }

    @Transactional(readOnly = true)
    public List<PageViewDto> getPageView() {
        return moveViewRepository.getPageViewInfo();
    }


    @Transactional(readOnly = true)
    public void sendPush(PushRequestDto pushRequestDto) throws FirebaseMessagingException {
        //유저 정보 조회
        User userInfo = userRepository.findByUserEmail(pushRequestDto.getUserEmail())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));
        //푸시 전송
        UseClause useClause = useClauseRepository.findByUseClauseKindAndUseClauseState(Constants.USE_CLAUSE_KIND.ADVERTISEMENT_PUSH, Constants.USE_CLAUSE_STATE.APPLY)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USE_CLAUSE.getMessage()));
        UserUseClause targetUser = userUseClauseRepository.findByUserAndUseClauseAndAgreeYn(userInfo, useClause, Constants.YN.Y)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USER_USE_CLAUSE.getMessage()));
        if(pushRequestDto.getUserEmail().equals(targetUser.getUser().getUserEmail())){
            firebaseMessaging.send(makeMessage(userInfo.getUserPushToken(), pushRequestDto.getTitle(), pushRequestDto.getContent()));
        }else{
            throw new CommonException(CommonErrorCode.NOT_FOUND_USER_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USER_USE_CLAUSE.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public void sendPushs(PushsRequestDto pushsRequestDto) throws FirebaseMessagingException {
        //유저 정보 조회
        List<User> userInfos = userRepository.findAllByUserEmailNotNull();
        UseClause useClause = useClauseRepository.findByUseClauseKindAndUseClauseState(Constants.USE_CLAUSE_KIND.ADVERTISEMENT_PUSH, Constants.USE_CLAUSE_STATE.APPLY)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USE_CLAUSE.getMessage()));
        List<UserUseClause> targetUsers = userUseClauseRepository.findAllByUserClauseAndAgreeYn(useClause, Constants.YN.Y);
        List<String> targetUserTokens = new ArrayList<>();
        targetUsers.forEach(targetUser -> targetUserTokens.add(targetUser.getUser().getUserPushToken()));
        FirebaseMessaging.getInstance().sendEachForMulticast(makeMessages(pushsRequestDto.getTitle(), pushsRequestDto.getContent(),targetUserTokens));

    }
}
