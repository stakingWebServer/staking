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

import static kr.project.backend.common.PushContent.*;
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
    private final UserAlarmSetRepository userAlarmSetRepository;


    @Transactional(readOnly = true)
    public TodayCommonResponseDto getTodayInfos(String todayStatus) {
        switch (todayStatus) {
            case "register" -> {
                int todayRegister = userRepository.countByCreatedDateBetween(
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return new TodayCommonResponseDto(todayRegister);
            }
            case "loginUser" -> {
                int todayLoginUser = userRepository.countByUserLoginDttmBetween(
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                return new TodayCommonResponseDto(todayLoginUser);
            }
            case "dropUser" -> {
                int todayDropUser = dropUserRepository.countByDropDttmBetween(
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                return new TodayCommonResponseDto(todayDropUser);
            }
            default -> {
                throw new CommonException(CommonErrorCode.COMMON_FAIL.getCode(), CommonErrorCode.COMMON_FAIL.getMessage());
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PageViewDto> getPageView() {
        return moveViewRepository.getPageViewInfo();
    }


    @Transactional
    public void sendPush(PushRequestDto pushRequestDto) throws FirebaseMessagingException {
        if (TimeRestriction.checkTimeRestriction()) {
            throw new CommonException(CommonErrorCode.NOT_SEND_TIME.getCode(), CommonErrorCode.NOT_SEND_TIME.getMessage());
        } else {
            //유저 정보 조회
            User userInfo = userRepository.findByUserEmail(pushRequestDto.getUserEmail())
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));
            //푸시 전송
            UserAlarmSet targetUser = userAlarmSetRepository.findByUserAndAlarmKindAndAlarmSetYn(userInfo,Constants.ALARM_KIND.APP_IN_PUSH,Constants.YN.Y)
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

            if (!targetUser.getUser().getUserPushToken().isBlank()) {
                alarmRepository.save(new Alarm(pushRequestDto.getTitle(), pushRequestDto.getContent(), userInfo));
                long alarmCnt = alarmRepository.countByUserAndAlarmReadYn(userInfo,Constants.YN.N);
                firebaseMessaging.send(makeMessage(targetUser.getUser().getUserPushToken(), pushRequestDto.getTitle(), pushRequestDto.getContent(),alarmCnt));
            }else{
                throw new CommonException(CommonErrorCode.FAIL_PUSH.getCode(), CommonErrorCode.FAIL_PUSH.getMessage());
            }
        }
    }
    @Transactional
    public void sendPushs(PushsRequestDto pushsRequestDto) throws FirebaseMessagingException {
        if (TimeRestriction.checkTimeRestriction()) {
            throw new CommonException(CommonErrorCode.NOT_SEND_TIME.getCode(), CommonErrorCode.NOT_SEND_TIME.getMessage());
        } else {
            //push data list
            List<String> targetUserTokens = new ArrayList<>();
            List<Integer> targetUserAlarmCnt = new ArrayList<>();
            //광고성 push
            if(pushsRequestDto.getAdvertisementPushYn().equals(Constants.YN.Y)){
                //마케팅동의 약관ID 조회
                UseClause useClause = useClauseRepository.findByUseClauseKindAndUseClauseState(Constants.USE_CLAUSE_KIND.ADVERTISEMENT_PUSH, Constants.USE_CLAUSE_STATE.APPLY)
                        .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USE_CLAUSE.getCode(), CommonErrorCode.NOT_FOUND_USE_CLAUSE.getMessage()));
                //마케팅동의 약관 동의자 조회
                List<UserUseClause> userUseClauses = userUseClauseRepository.findAllByUseClauseAndAgreeYn(useClause, Constants.YN.Y);
                //앱 알림 동의자 조회
                userUseClauses.forEach(targetUser -> {
                    UserAlarmSet userAlarmSet = userAlarmSetRepository.findByUserAndAlarmKindAndAlarmSetYn(targetUser.getUser(),Constants.ALARM_KIND.APP_IN_PUSH,Constants.YN.Y)
                            .orElse(null);

                    if ( userAlarmSet != null && (!targetUser.getUser().getUserPushToken().isBlank()) ) {
                        //알람 DB 저장.
                        alarmRepository.save(new Alarm(pushsRequestDto.getTitle(), pushsRequestDto.getContent(), targetUser.getUser()));
                        //읽지 않은 알람 조회
                        long alarmCnt = alarmRepository.countByUserAndAlarmReadYn(targetUser.getUser(),Constants.YN.N);
                        targetUserAlarmCnt.add((int) alarmCnt);
                        targetUserTokens.add(targetUser.getUser().getUserPushToken());
                    }
                });

            //비광고성 push
            }else{
                //앱 알림 동의자 조회
                List<User> user = userRepository.findByUserPushTokenNot("");
                user.forEach(targetUser -> {
                    UserAlarmSet userAlarmSet = userAlarmSetRepository.findByUserAndAlarmKindAndAlarmSetYn(targetUser,Constants.ALARM_KIND.APP_IN_PUSH,Constants.YN.Y)
                            .orElse(null);

                    if ( userAlarmSet != null) {
                        //알람 DB 저장.
                        alarmRepository.save(new Alarm(pushsRequestDto.getTitle(), pushsRequestDto.getContent(), targetUser));
                        //읽지 않은 알람 조회
                        long alarmCnt = alarmRepository.countByUserAndAlarmReadYn(targetUser,Constants.YN.N);
                        targetUserAlarmCnt.add((int) alarmCnt);
                        targetUserTokens.add(targetUser.getUserPushToken());
                    }
                });
            }

            //단체 푸시 전송.
            //FirebaseMessaging.getInstance().sendEachForMulticast(makeMessages(pushsRequestDto.getTitle(), pushsRequestDto.getContent(), targetUserTokens));
            firebaseMessaging.sendEachAsync(makeMessageSendAll(pushsRequestDto.getTitle(), pushsRequestDto.getContent(), targetUserTokens,targetUserAlarmCnt));
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
  /*      if (TimeRestriction.checkTimeRestriction()) {
            throw new CommonException(CommonErrorCode.NOT_SEND_TIME.getCode(), CommonErrorCode.NOT_SEND_TIME.getMessage());
        } else {*/
            Questions questionInfo = questionRepository.findById(replyRequestDto.getQuestionId())
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_QUESTION.getCode(), CommonErrorCode.NOT_FOUND_QUESTION.getMessage()));
            User userInfo = questionInfo.getUser();

            //답변 DB 저장.
            String replyId = replyRepository.save(new Reply(replyRequestDto,questionInfo)).getReplyId();

            //알람 DB 저장.
            alarmRepository.save(new Alarm("[문의] 답변 도착", replyRequestDto.getContent(), userInfo, Constants.ALARM_DETAIL_KIND.REPLY, replyId));

            //push 발송
            long alarmCnt = alarmRepository.countByUserAndAlarmReadYn(userInfo,Constants.YN.N);
            firebaseMessaging.send(makeMessage(userInfo.getUserPushToken(), "[문의] 답변 도착", replyRequestDto.getContent(),alarmCnt));
        }
   // }

    public AccessKeyResponseDto getApikey(AdminLoginRequestDto adminLoginRequestDto) throws Exception {
        if (adminLoginRequestDto.getLoginId().equals(loginId) && adminLoginRequestDto.getPassword().equals(password)) {
            return new AccessKeyResponseDto(encryptAES256(adminAESKey, adminAESIv, "stakingAdmin" + System.currentTimeMillis()), "ok");
        }
        throw new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage());
    }

}
