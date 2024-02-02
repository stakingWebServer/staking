package kr.project.backend.service.admin;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import kr.project.backend.common.CommonErrorCode;
import kr.project.backend.common.CommonException;
import kr.project.backend.common.Constants;
import kr.project.backend.dto.admin.request.AdminLoginRequestDto;
import kr.project.backend.dto.admin.request.ReplyRequestDto;
import kr.project.backend.dto.admin.response.*;
import kr.project.backend.dto.common.request.PushRequestDto;
import kr.project.backend.dto.common.request.PushsRequestDto;
import kr.project.backend.entity.common.CommonFile;
import kr.project.backend.entity.user.*;
import kr.project.backend.repository.user.*;
import kr.project.backend.utils.TimeRestriction;
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

    @Value("${admin.loginId}")
    private String loginId;
    @Value("${admin.password}")
    private String password;

    private final UserRepository userRepository;
    private final DropUserRepository dropUserRepository;
    private final MoveViewRepository moveViewRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final UseClauseRepository useClauseRepository;
    private final UserUseClauseRepository userUseClauseRepository;
    private final QuestionRepository questionRepository;
    private final ReplyRepository replyRepository;
    private final AlarmRepository alarmRepository;

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
        if (TimeRestriction.checkTimeRestriction()) {
            throw new CommonException(CommonErrorCode.NOT_SEND_TIME.getCode(), CommonErrorCode.NOT_SEND_TIME.getMessage());
        } else {
            //유저 정보 조회
            User userInfo = userRepository.findByUserEmail(pushRequestDto.getUserEmail())
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));
            //푸시 전송
            UseClause useClause = useClauseRepository.findByUseClauseKindAndUseClauseState(Constants.USE_CLAUSE_KIND.ADVERTISEMENT_PUSH, Constants.USE_CLAUSE_STATE.APPLY)
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USE_CLAUSE.getMessage()));
            UserUseClause targetUser = userUseClauseRepository.findByUserAndUseClauseAndAgreeYn(userInfo, useClause, Constants.YN.Y)
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USER_USE_CLAUSE.getMessage()));
            if (pushRequestDto.getUserEmail().equals(targetUser.getUser().getUserEmail())) {
                if (!targetUser.getUser().getUserPushToken().isBlank()) {
                    firebaseMessaging.send(makeMessage(targetUser.getUser().getUserPushToken(), pushRequestDto.getTitle(), pushRequestDto.getContent()));
                    alarmRepository.save(new Alarm(pushRequestDto.getTitle(), pushRequestDto.getContent(), userInfo));
                }
            } else {
                throw new CommonException(CommonErrorCode.NOT_FOUND_USER_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USER_USE_CLAUSE.getMessage());
            }
        }
    }

    @Transactional(readOnly = true)
    public void sendPushs(PushsRequestDto pushsRequestDto) throws FirebaseMessagingException {
        if (TimeRestriction.checkTimeRestriction()) {
            throw new CommonException(CommonErrorCode.NOT_SEND_TIME.getCode(), CommonErrorCode.NOT_SEND_TIME.getMessage());
        } else {
            UseClause useClause = useClauseRepository.findByUseClauseKindAndUseClauseState(Constants.USE_CLAUSE_KIND.ADVERTISEMENT_PUSH, Constants.USE_CLAUSE_STATE.APPLY)
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USE_CLAUSE.getMessage()));
            List<UserUseClause> targetUsers = userUseClauseRepository.findAllByUseClauseAndAgreeYn(useClause, Constants.YN.Y);
            List<String> targetUserTokens = new ArrayList<>();
            targetUsers.forEach(targetUser -> {
                if (!targetUser.getUser().getUserPushToken().isBlank()) {
                    targetUserTokens.add(targetUser.getUser().getUserPushToken());
                    //알림 DB 저장.
                    alarmRepository.save(new Alarm(pushsRequestDto.getTitle(), pushsRequestDto.getContent(), targetUser.getUser()));
                }
            });
            //단체 푸시 전송.
            FirebaseMessaging.getInstance().sendEachForMulticast(makeMessages(pushsRequestDto.getTitle(), pushsRequestDto.getContent(), targetUserTokens));

        }
    }

    @Transactional(readOnly = true)
    public List<QuestionResponseDto> getQuestions() {
        List<Questions> questions = questionRepository.findAll();
        List<QuestionResponseDto> responses = new ArrayList<>();
        questions.forEach(question -> {
            List<CommonFile> commonFiles = question.getCommonGroupFile().getCommonFileList();
            List<QuestionFileInfoDto> questionFileInfoDtos = new ArrayList<>();
            commonFiles.forEach(file -> {
                questionFileInfoDtos.add(new QuestionFileInfoDto(file.getFileName(), file.getFileUrl()));
            });
            boolean replyCheck = replyRepository.existsByQuestions(question);
            responses.add(new QuestionResponseDto(question.getQuestionId(), question.getTitle(), question.getContent(), questionFileInfoDtos, replyCheck ? String.valueOf(question.getReply().isReplyYn()) : "N"));
        });
        return responses;
    }

    @Transactional
    public void replyAboutQuestion(ReplyRequestDto replyRequestDto) throws FirebaseMessagingException {
        if (TimeRestriction.checkTimeRestriction()) {
            throw new CommonException(CommonErrorCode.NOT_SEND_TIME.getCode(), CommonErrorCode.NOT_SEND_TIME.getMessage());
        } else {
            Questions questionInfo = questionRepository.findById(replyRequestDto.getQuestionId())
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_QUESTION.getCode(), CommonErrorCode.NOT_FOUND_QUESTION.getMessage()));
            User userInfo = questionInfo.getUser();

            //토큰 발송
            //TODO 문의에 대한 답변 후 푸시알림 제목 뭐로 보낼지 고민.
            firebaseMessaging.send(makeMessage(userInfo.getUserPushToken(), "관리자", replyRequestDto.getContent()));

            //알림 DB 저장.
            alarmRepository.save(new Alarm("관리자", replyRequestDto.getContent(), userInfo));
            //답변 DB 저장.
            replyRepository.save(new Reply(replyRequestDto));
        }
    }

    public AccessKeyResponseDto getApikey(AdminLoginRequestDto adminLoginRequestDto) throws Exception {
        if (adminLoginRequestDto.getLoginId().equals(loginId) && adminLoginRequestDto.getPassword().equals(password)) {
            return new AccessKeyResponseDto(encryptAES256(adminAESKey, adminAESIv, "stakingAdmin" + System.currentTimeMillis()), "ok");
        }
        return new AccessKeyResponseDto(null, "fail");
    }
}
