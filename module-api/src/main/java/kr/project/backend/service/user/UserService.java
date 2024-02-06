package kr.project.backend.service.user;

import io.jsonwebtoken.ExpiredJwtException;
import kr.project.backend.auth.ServiceUser;
import kr.project.backend.dto.user.response.UseClauseResponseDto;
import kr.project.backend.dto.user.response.*;
import kr.project.backend.entity.common.CommonFile;
import kr.project.backend.entity.common.CommonGroupFile;
import kr.project.backend.repository.common.CommonFileRepository;
import kr.project.backend.repository.common.CommonGroupFileRepository;
import kr.project.backend.repository.common.QuestionsRepository;
import kr.project.backend.utils.JwtUtil;
import kr.project.backend.entity.common.CommonCode;
import kr.project.backend.common.CommonErrorCode;
import kr.project.backend.common.CommonException;
import kr.project.backend.common.Constants;
import kr.project.backend.dto.user.request.*;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.user.*;
import kr.project.backend.repository.coin.StakingInfoRepository;
import kr.project.backend.repository.common.CommonCodeRepository;
import kr.project.backend.repository.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;
    private final long expiredHs = 3600000; //1hr

    private final long accesTokenTime = 24L; //1day (1hr * 24)

    private final long refreshTokenTime = 168L; //7day (1hr * 168)

    private final Integer reJoinTermDate = 30;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DropUserRepository dropUserRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final StakingInfoRepository stakingInfoRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserUseClauseRepository userUseClauseRepository;
    private final UseClauseRepository useClauseRepository;
    private final AppVersionRepository appVersionRepository;
    private final MoveViewRepository moveViewRepository;
    private final NoticeRepository noticeRepository;
    private final AlarmRepository alarmRepository;
    private final MyStakingDataAboutRewardRepository myStakingDataAboutRewardRepository;
    private final UserNoticeReadRepository userNoticeReadRepository;
    private final CommonFileRepository commonFileRepository;
    private final QuestionsRepository questionsRepository;
    private final CommonGroupFileRepository commonGroupFileRepository;
    private final ReplyRepository replyRepository;
    private final UserAlarmSetRepository userAlarmSetRepository;

    @Transactional
    public UserTokenResponseDto userLogin(UserLoginRequestDto userLoginRequestDto) {

        //등록되어 있는 유저인지 아닌지 판단
        boolean checkUserInfo = userRepository.existsByUserEmail(userLoginRequestDto.getUserEmail());
 
        //등록되어 있지 않는 유저
        if (!checkUserInfo) {

            //회원가입 1달 제한 정책 체크(탈퇴 테이블 조회)
            Page<DropUser> dropCheck = dropUserRepository.findByUserEmail(userLoginRequestDto.getUserEmail(), PageRequest.of(0, 1));

            if (dropCheck.getContent().size() != 0) {
                try {
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dropDate = transFormat.parse(String.valueOf(dropCheck.getContent().get(0)));
                    Date nowDate = transFormat.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                    Long dropAndJoinTerm = (nowDate.getTime() - dropDate.getTime()) / 1000 / (24 * 60 * 60);

                    if (reJoinTermDate > dropAndJoinTerm.intValue()) {
                        //한달제한 임시 해제
                        //throw new CommonException(CommonErrorCode.JOIN_TERM_DATE.getCode(), CommonErrorCode.JOIN_TERM_DATE.getMessage());
                    }
                } catch (ParseException e) {
                    log.info("date 변환 파싱 error");
                }
            }

            //회원가입 안내 return
            throw new CommonException(CommonErrorCode.NOT_JOIN_USER.getCode(), CommonErrorCode.NOT_JOIN_USER.getMessage());
        }
        //등록되어 있는 유저

        //정보 조회
        User userInfo = userRepository.findByUserEmail(userLoginRequestDto.getUserEmail())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //중복 회원가입 체크
        if (!userInfo.getUserJoinSnsKind().equals(userLoginRequestDto.getUserJoinSnsKind())) {
            CommonCode commonCode = commonCodeRepository.findByGrpCommonCodeAndCommonCode(Constants.USER_JOIN_SNS_KIND.CODE, userInfo.getUserJoinSnsKind())
                    .orElseThrow(() -> new CommonException(CommonErrorCode.NULL_DATA.getCode(), CommonErrorCode.NULL_DATA.getMessage()));
            ;
            throw new CommonException(CommonErrorCode.ALREADY_JOIN_USER.getCode(), CommonErrorCode.ALREADY_JOIN_USER.getMessage() + "(" + commonCode.getCommonCodeName() + ")");
        }

        //로그인시간, os, pushToken 업데이트
        userInfo.updateUserLoginInfo(userLoginRequestDto);
        userRepository.save(userInfo);


        String accessToken = JwtUtil.createJwt(userInfo.getUserId(), userInfo.getUserEmail(), jwtSecretKey, expiredHs * accesTokenTime);
        String refreshToken = JwtUtil.createJwt(userInfo.getUserId(), userInfo.getUserEmail(), jwtSecretKey, expiredHs * refreshTokenTime);

        RefreshToken refreshTokenInfo = refreshTokenRepository.findByUser(userInfo)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_EXIST_TOKEN.getCode(), CommonErrorCode.NOT_EXIST_TOKEN.getMessage()));

        //dirty checking 으로 인한 리프레시토큰 업데이트
        refreshTokenInfo.updateRefreshToken(refreshToken);

        refreshTokenRepository.save(refreshTokenInfo);

        return new UserTokenResponseDto(accessToken, String.valueOf(refreshTokenInfo.getRefreshTokenId()));
    }

    @Transactional
    public UserTokenResponseDto userJoin(UserJoinRequestDto userJoinRequestDto) {

        //등록되어 있는 유저인지 아닌지 판단
        boolean checkUserInfo = userRepository.existsByUserEmail(userJoinRequestDto.getUserEmail());

        if (checkUserInfo) {
            throw new CommonException(CommonErrorCode.ALREADY_JOIN_USER.getCode(), CommonErrorCode.ALREADY_JOIN_USER.getMessage());
        }

        //회원가입
        String userId = userRepository.save(new User(userJoinRequestDto)).getUserId();

        //방금 회원가입 된 유저 정보 가져오기
        User userInfo = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //이용약관 동의 저장
        for (int i = 0; i < userJoinRequestDto.getUseClauseDtoList().size(); i++) {
            UseClauseDto useClauseDto = userJoinRequestDto.getUseClauseDtoList().get(i);
            UseClause useClause = useClauseRepository.findById(useClauseDto.getUseClauseId())
                    .orElseThrow(()->new CommonException(CommonErrorCode.NOT_FOUND_USE_CLAUSE_DATA.getCode(),CommonErrorCode.NOT_FOUND_USE_CLAUSE_DATA.getMessage()));
            //필수약관이지만 동의여부가 N으로 요청왔을때
            if(useClause.getUseClauseEssentialYn().equals(Constants.YN.Y) && useClauseDto.getUseClauseAgreeYN().equals(Constants.YN.N)){
                throw new CommonException(CommonErrorCode.NOT_AGREE_ESSENTIAL_USE_CLAUSE.getCode(),CommonErrorCode.NOT_AGREE_ESSENTIAL_USE_CLAUSE.getMessage());
            }
            userUseClauseRepository.save(new UserUseClause(userInfo, useClause, useClauseDto));
        }

        //앱 푸시 알림 저장
        UserAlarmSetRequestDto userAlarmSetRequestDto = new UserAlarmSetRequestDto();
        userAlarmSetRequestDto.setAlarmKind(Constants.ALARM_KIND.APP_IN_PUSH);
        userAlarmSetRequestDto.setAlarmSetYn(Constants.YN.Y);
        userAlarmSetRepository.save(new UserAlarmSet(userAlarmSetRequestDto, userInfo));

        //응답 토큰 세팅(리스레시 토큰은 키값으로 응답)
        String accessToken = JwtUtil.createJwt(userId, userInfo.getUserEmail(), jwtSecretKey, expiredHs * accesTokenTime);
        String refreshToken = JwtUtil.createJwt(userId, userInfo.getUserEmail(), jwtSecretKey, expiredHs * refreshTokenTime);

        //리프레시 토큰 저장
        refreshTokenRepository.save(new RefreshToken(refreshToken, userInfo));

        RefreshToken refreshTokenInfo = refreshTokenRepository.findByUser(userInfo)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_EXIST_TOKEN.getCode(), CommonErrorCode.NOT_EXIST_TOKEN.getMessage()));

        return new UserTokenResponseDto(accessToken, String.valueOf(refreshTokenInfo.getRefreshTokenId()));
    }

    @Transactional
    public UserTokenResponseDto refreshAuthorize(UserRefreshTokenRequestDto userRefreshTokenRequestDto) {

        //리프레시토큰키값으로 리프레시 토큰 조회
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshTokenId(userRefreshTokenRequestDto.getRefreshTokenId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_EXIST_TOKEN.getCode(), CommonErrorCode.NOT_EXIST_TOKEN.getMessage()));

        //회원정보
        User userInfo = userRepository.findById(refreshToken.getUser().getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //토큰 유효성검사
        try {
            JwtUtil.isExpired(refreshToken.getRefreshToken(), jwtSecretKey);
        } catch (ExpiredJwtException e) {
            //만료시 refreshToken 재발급(db 업데이트)
            String newRefreshToken = JwtUtil.createJwt(userInfo.getUserId(), userInfo.getUserEmail(), jwtSecretKey, expiredHs * refreshTokenTime);

            //리프레시 토큰 저장
            refreshToken.updateRefreshToken(newRefreshToken);
        }

        //토큰 재발급
        String accessToken = JwtUtil.createJwt(userInfo.getUserId(), userInfo.getUserEmail(), jwtSecretKey, expiredHs * accesTokenTime);
        String refreshTokenId = String.valueOf(refreshToken.getRefreshTokenId());

        return new UserTokenResponseDto(accessToken, refreshTokenId);
    }

    @Transactional
    public void logout(ServiceUser serviceUser) {

        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //로그아웃시간, 푸시토큰 null 업데이트
        userInfo.updateUserLogoutDttmAndPushToken(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        userRepository.save(userInfo);
    }

    @Transactional
    public void dropUser(ServiceUser serviceUser) {

        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //탈퇴 중복 처리 방어로직
        if (userInfo.getUserState().equals(Constants.USER_STATE.DROP_USER)) {
            throw new CommonException(CommonErrorCode.ALREADY_DROP_USER.getCode(), CommonErrorCode.ALREADY_DROP_USER.getMessage());
        }

        RefreshToken refreshToken = refreshTokenRepository.findByUser(userInfo)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_TOKEN.getCode(), CommonErrorCode.NOT_FOUND_TOKEN.getMessage()));


        //탈퇴 테이블 저장
        dropUserRepository.save(new DropUser(userInfo));

        //회원상태 업데이트(기본 개인정보 삭제)
        userInfo.updateUserDrop();
        userRepository.save(userInfo);

        //리프레시 테이블 삭제
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional(readOnly = true)
    public UserCheckStateResponseDto userStateCheck(ServiceUser serviceUser) {

        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        CommonCode commonCode = commonCodeRepository.findByGrpCommonCodeAndCommonCode(Constants.USER_STATE.CODE, userInfo.getUserState())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NULL_DATA.getCode(), CommonErrorCode.NULL_DATA.getMessage()));
        ;

        return new UserCheckStateResponseDto(userInfo.getUserState(), commonCode.getCommonCodeName());
    }

    @Transactional
    public void addFavorite(ServiceUser serviceUser, AddFavoriteRequestDto addFavoriteRequestDto) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));
        //코인정보
        StakingInfo stakingInfo = stakingInfoRepository.findById(addFavoriteRequestDto.getStakingId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(), CommonErrorCode.NOT_FOUND_COIN.getMessage()));

        //데이터 중복 방지 코드
        boolean favorite = favoriteRepository.existsByStakingInfoAndUserAndDelYn(stakingInfo, userInfo, false);
        if (favorite) {
            throw new CommonException(CommonErrorCode.ALREADY_EXIST_STAKING_DATA.getCode(), CommonErrorCode.ALREADY_EXIST_STAKING_DATA.getMessage());
        }
        favoriteRepository.save(new Favorite(userInfo, stakingInfo));
    }

    @Transactional
    public void unFavorite(ServiceUser serviceUser, UnFavoriteRequestDto unFavoriteRequestDto) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        StakingInfo stakingInfo = stakingInfoRepository.findById(unFavoriteRequestDto.getStakingId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(), CommonErrorCode.NOT_FOUND_COIN.getMessage()));
        //즐겨찾기 정보
        Favorite favorite = favoriteRepository.findByStakingInfoAndUserAndDelYn(stakingInfo, userInfo, false)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_FAVORITE.getCode(), CommonErrorCode.NOT_FOUND_FAVORITE.getMessage()));

        //즐겨찾기 헤제.
        favorite.unFavorite();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponseDto> getFavorites(ServiceUser serviceUser) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));


        return favoriteRepository.findAllByUserAndDelYn(userInfo, false)
                .stream()
                .map(FavoriteResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UseClauseResponseDto> getUseClauses() {
        //이용약관 목록조회
        return useClauseRepository.getUseClauses(Constants.USE_CLAUSE_KIND.CODE, Constants.USE_CLAUSE_STATE.APPLY);
    }

    @Transactional(readOnly = true)
    public AppVersionResponseDto getAppVersion(String appOs, String appVersion) {
        //강제업데이트 조회
        AppVersion appVersionData = appVersionRepository.findByAppOsAndMinimumVersionGreaterThanAndHardUpdateYnTrue(appOs, appVersion).orElse(null);

        AppVersionResponseDto appVersionResponseDto = new AppVersionResponseDto();

        if (appVersionData == null) {
            appVersionResponseDto.setHardUpdateYn(Constants.YN.N);
        } else {
            appVersionResponseDto.setHardUpdateYn(Constants.YN.Y);
            appVersionResponseDto.setHardUpdateUrl(appVersionData.getHardUpdateUrl());
        }
        return appVersionResponseDto;
    }

    @Transactional
    public void moveView(ServiceUser serviceUser, MoveViewRequestDto moveViewRequestDto) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //화면이동 저장
        moveViewRepository.save(new MoveView(userInfo, moveViewRequestDto));
    }

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getNotices(Pageable pageable,ServiceUser serviceUser) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        Page<Notice> noticeList = noticeRepository.findAllByOrderByCreatedDateDesc(pageable);
        List<NoticeResponseDto> noticeResponseDtoList = new ArrayList<>();
        noticeList.forEach(notice -> {
            boolean noticeReadChk = userNoticeReadRepository.existsByUserAndNotice(userInfo,notice);
            noticeResponseDtoList.add(new NoticeResponseDto(notice, noticeReadChk ? "Y" : "N"));
        });

        return noticeResponseDtoList;
    }

    @Transactional(readOnly = true)
    public List<?> getMydataStakings(ServiceUser serviceUser) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        return favoriteRepository.findAllByUserAndDelYn(userInfo,false)
                .stream()
                .map(MyStakingDataResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<AlarmResponseDto> getAlarms(Pageable pageable,ServiceUser serviceUser) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        return alarmRepository.findByUserOrderByCreatedDateDesc(userInfo,pageable)
                .stream()
                .map(AlarmResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void alarmRead(ServiceUser serviceUser, AlarmReadRequestDto alarmReadRequestDto){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        Alarm alarm = alarmRepository.findByAlarmIdAndUser(alarmReadRequestDto.getAlarmId(),userInfo)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NULL_DATA.getCode(),CommonErrorCode.NULL_DATA.getMessage()));

        alarm.updateAlarmReadYn();
        alarmRepository.save(alarm);
    }

    @Transactional
    public void ownCoin(ServiceUser serviceUser, OwnCoinRequestDto ownCoinRequestDto){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));


        StakingInfo stakingInfo = stakingInfoRepository.findById(ownCoinRequestDto.getStakingId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(),CommonErrorCode.NOT_FOUND_COIN.getMessage()));

        //보상주기 없으면 return
        if(stakingInfo.getRewardCycle() == null){
            throw new CommonException(CommonErrorCode.NOT_INPUT_COIN.getCode(),CommonErrorCode.NOT_INPUT_COIN.getMessage());
        }

        //최소신청수량 return
        //최소신청수량에서 숫자랑 소수점만 추출
        String regex = "[0-9.]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stakingInfo.getMinimumOrderQuantity());
        String regexTotalHoldings = "";
        while (matcher.find()) {
            regexTotalHoldings = matcher.group();
        }
        BigDecimal minimumOrderQuantity = new BigDecimal(regexTotalHoldings);
        BigDecimal totalHoldings = new BigDecimal(ownCoinRequestDto.getTotalHoldings());
        if(totalHoldings.compareTo(minimumOrderQuantity) < 0){
            throw new CommonException(CommonErrorCode.CHECK_MIN_INPUT_COIN.getCode(),CommonErrorCode.CHECK_MIN_INPUT_COIN.getMessage());
        }

        Favorite favorite = favoriteRepository.findByStakingInfoAndUserAndDelYn(stakingInfo, userInfo,false)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_FAVORITE.getCode(), CommonErrorCode.NOT_FOUND_FAVORITE.getMessage()));

        favorite.updateTotalHoldings(new BigDecimal(ownCoinRequestDto.getTotalHoldings()));
        //총보유수량 업데이트
        favoriteRepository.save(favorite);
        //보상내역 저장
        myStakingDataAboutRewardRepository.save(new MyStakingDataAboutReward(favorite,userInfo,new BigDecimal("0"), new BigDecimal(ownCoinRequestDto.getTotalHoldings())));
    }

    @Transactional(readOnly = true)
    public StakingsDetailResponseDto stakingsDetail(ServiceUser serviceUser, String stakingId, String historyDate){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        StakingInfo stakingInfo = stakingInfoRepository.findById(stakingId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(),CommonErrorCode.NOT_FOUND_COIN.getMessage()));

        Favorite favorite = favoriteRepository.findByStakingInfoAndUserAndDelYn(stakingInfo,userInfo,false)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_FAVORITE.getCode(), CommonErrorCode.NOT_FOUND_FAVORITE.getMessage()));

        List<MyStakingDataAboutReward> myStakingDataAboutReward = new ArrayList<>();

        if(historyDate != null && !historyDate.equals("04")){
            LocalDateTime past = LocalDateTime.now();
            if(historyDate.equals("01")){
                past = LocalDateTime.now().minusWeeks(1);
            }else if(historyDate.equals("02")){
                past = LocalDateTime.now().minusMonths(1);
            }else if(historyDate.equals("03")){
                past = LocalDateTime.now().minusMonths(6);
            }
            String startDate = LocalDateTime.of(past.toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String endDate = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            myStakingDataAboutReward = myStakingDataAboutRewardRepository.findByFavoriteAndUserAndCreatedDateBetween(favorite,userInfo,startDate,endDate);
        }else{
            myStakingDataAboutReward = myStakingDataAboutRewardRepository.findByFavoriteAndUser(favorite,userInfo);
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##################");

        return new StakingsDetailResponseDto(stakingInfo.getCoinName(),decimalFormat.format(favorite.getTotalHoldings()),decimalFormat.format(favorite.getTotalRewards()),stakingInfo.getUnit(),
                myStakingDataAboutReward.stream().map(MyStakingDataAboutRewardHistoryDto::new).collect(Collectors.toList())
                );
    }

    @Transactional
    public void noticeRead(ServiceUser serviceUser, NoticeReadRequestDto noticeReadRequestDto){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        Notice notice = noticeRepository.findById(noticeReadRequestDto.getNoticeId())
                .orElseThrow(()-> new CommonException(CommonErrorCode.NOT_FOUND_NOTICE.getCode(),CommonErrorCode.NOT_FOUND_NOTICE.getMessage()));

        boolean noticeReadCheck = userNoticeReadRepository.existsByUserAndNotice(userInfo,notice);

        if(!noticeReadCheck){
            userNoticeReadRepository.save(new UserNoticeRead(userInfo,notice));
        }
    }

    @Transactional
    public void question(ServiceUser serviceUser, QuestionRequestDto questionRequestDto){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));


        //그룹파일ID
        String groupFileId = "";
        CommonGroupFile commonGroupFile = null;

        //중복체크
        boolean questionsCheck = false;

        //문의하기 파일 첨부시에만
        if(questionRequestDto.getFileList() != null){
            //파일 체크
            for(QuestionRequestDto.file file : questionRequestDto.getFileList()){
                CommonFile commonFile = commonFileRepository.findByFileId(file.getFileId())
                        .orElseThrow(() -> new CommonException(CommonErrorCode.$_NOT_FOUND_FILE.getCode(),
                                CommonErrorCode.$_NOT_FOUND_FILE.getMessage().replace("[$fileId]",file.getFileId())));
                groupFileId = commonFile.getCommonGroupFile().getGroupFileId();
            }

            //그룹파일로 묶어주기
            for(QuestionRequestDto.file file : questionRequestDto.getFileList()){
                CommonFile commonFile = commonFileRepository.findByFileId(file.getFileId())
                        .orElseThrow(() -> new CommonException(CommonErrorCode.$_NOT_FOUND_FILE.getCode(),
                                CommonErrorCode.$_NOT_FOUND_FILE.getMessage().replace("[$fileId]",file.getFileId())));
                commonGroupFile = commonGroupFileRepository.findById(groupFileId).orElse(null);
                commonFile.updateGroupFileId(commonGroupFile);
                commonFileRepository.save(commonFile);
                questionsCheck = questionsRepository.existsByCommonGroupFile(commonGroupFile);
            }
        }

        //문의사항 저장
        if(!questionsCheck){
            questionsRepository.save(new Questions(questionRequestDto,commonGroupFile,userInfo));
        }

    }

    @Transactional(readOnly = true)
    public List<QuestionsResponseDto> getQuestions(Pageable pageable, ServiceUser serviceUser){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        Page<Questions> questionsList = questionsRepository.findByUserOrderByCreatedDateDesc(userInfo,pageable);

        List<QuestionsResponseDto> response = new ArrayList<>();

        for(Questions questions : questionsList){
            CommonGroupFile commonGroupFile = null;
            List<CommonFile> commonFile = new ArrayList<>();
            if(questions.getCommonGroupFile() != null){
                commonGroupFile = commonGroupFileRepository.findById(questions.getCommonGroupFile().getGroupFileId()).orElse(null);
                commonFile = commonFileRepository.findByCommonGroupFileOrderByCreatedDate(commonGroupFile);
            }
            QuestionsResponseDto questionsResponseDto = new QuestionsResponseDto(questions,
                    commonFile.stream().map(QuestionsFileResponseDto::new).collect(Collectors.toList()));
            response.add(questionsResponseDto);
        }

        return response;
    }

    @Transactional
    public void replyRead(ServiceUser serviceUser, ReplyReadRequstDto replyReadRequstDto){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        Reply reply = replyRepository.findById(replyReadRequstDto.getReplyId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_REPLY.getCode(), CommonErrorCode.NOT_FOUND_REPLY.getMessage()));

        reply.updateReplyReadYn();

        replyRepository.save(reply);
    }

    @Transactional
    public void alarmSet(ServiceUser serviceUser, UserAlarmSetRequestDto userAlarmSetRequestDto){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        if(userAlarmSetRequestDto.getAlarmKind().equals(Constants.ALARM_KIND.APP_IN_PUSH)){
            UserAlarmSet userAlarmSetChk = userAlarmSetRepository.findByUserAndAlarmKind(userInfo,userAlarmSetRequestDto.getAlarmKind()).orElse(null);
            if(userAlarmSetChk != null){
                userAlarmSetChk.updateAlarmSetYn(userAlarmSetRequestDto.getAlarmSetYn());
                userAlarmSetRepository.save(userAlarmSetChk);
            }else{
                userAlarmSetRepository.save(new UserAlarmSet(userAlarmSetRequestDto, userInfo));
            }
        }else if(userAlarmSetRequestDto.getAlarmKind().equals(Constants.ALARM_KIND.ADVERTISEMENT_PUSH)){
            UseClause useClause = useClauseRepository.findByUseClauseKindAndUseClauseState(Constants.USE_CLAUSE_KIND.ADVERTISEMENT_PUSH, Constants.USE_CLAUSE_STATE.APPLY).orElse(null);
            if(useClause != null){
                UserUseClause userUseClause = userUseClauseRepository.findByUserAndUseClause(userInfo, useClause).orElse(null);
                if(userUseClause != null){
                    userUseClause.updateAgreeYn(userAlarmSetRequestDto.getAlarmSetYn());
                    userUseClauseRepository.save(userUseClause);
                }else{
                    UseClauseDto useClauseDto = new UseClauseDto();
                    useClauseDto.setUseClauseId(useClause.getUseClauseId());
                    useClauseDto.setUseClauseAgreeYN(userAlarmSetRequestDto.getAlarmSetYn());
                    userUseClauseRepository.save(new UserUseClause(userInfo,useClause,useClauseDto));
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public AlarmCountResponseDto alarmCount(ServiceUser serviceUser){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        long alarmCnt = alarmRepository.countByUserAndAlarmReadYn(userInfo,Constants.YN.N);

        return new AlarmCountResponseDto(userInfo.getUserEmail(),alarmCnt>0 ? "Y":"N",alarmCnt);
    }

    @Transactional(readOnly = true)
    public List<UseClauseBeforeResponseDto> useClauseBefore(String useClauseKind){

        List<UseClause> useClause = useClauseRepository.findByUseClauseKindAndUseClauseStateOrderByCreatedDateDesc(useClauseKind, Constants.USE_CLAUSE_STATE.END);
        if(useClause.size() == 0){
            throw new CommonException(CommonErrorCode.NOT_FOUND_BEFORE_USE_CLAUSE.getCode(),CommonErrorCode.NOT_FOUND_BEFORE_USE_CLAUSE.getMessage());
        }

        return useClauseRepository.findByUseClauseKindAndUseClauseStateOrderByCreatedDateDesc(useClauseKind, Constants.USE_CLAUSE_STATE.END)
                .stream()
                .map(UseClauseBeforeResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlarmSetResponseDto> getAlarmSet(ServiceUser serviceUser){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        return userAlarmSetRepository.findByUser(userInfo)
                .stream()
                .map(AlarmSetResponseDto::new)
                .collect(Collectors.toList());
    }
}
