package kr.project.backend.service.user;

import io.jsonwebtoken.ExpiredJwtException;
import kr.project.backend.auth.ServiceUser;
import kr.project.backend.dto.user.response.UseClauseResponseDto;
import kr.project.backend.dto.user.response.*;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private final MyStakingDataRepository myStakingDataRepository;
    private final MyStakingDataAboutRewardRepository myStakingDataAboutRewardRepository;
    private final AlarmRepository alarmRepository;

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
            userUseClauseRepository.save(new UserUseClause(userInfo, useClauseDto));
        }

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

        //로그아웃시간 업데이트
        userInfo.updateUserLogoutDttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

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
    public AddFavoriteResponseDto addFavorite(ServiceUser serviceUser, AddFavoriteRequestDto addFavoriteRequestDto) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));
        //코인정보
        StakingInfo stakingInfo = stakingInfoRepository.findById(addFavoriteRequestDto.getStakingId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(), CommonErrorCode.NOT_FOUND_COIN.getMessage()));

        //데이터 중복 방지 코드
        boolean favorite = favoriteRepository.existsByStakingInfoAndUserAndDelYn(stakingInfo, userInfo, false);
        if (favorite) {
            throw new CommonException(CommonErrorCode.ALREADY_EXIST_FAVORITE.getCode(), CommonErrorCode.ALREADY_EXIST_FAVORITE.getMessage());
        }

        Favorite favoriteInfo = favoriteRepository.save(new Favorite(userInfo, stakingInfo));
        //favorite 키값 반환
        return new AddFavoriteResponseDto(favoriteInfo.getFavoriteId());
    }

    @Transactional
    public void unFavorite(ServiceUser serviceUser, UnFavoriteRequestDto unFavoriteRequestDto) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //즐겨찾기 정보
        Favorite favorite = favoriteRepository.findByFavoriteIdAndUserAndDelYn(unFavoriteRequestDto.getFavoriteId(), userInfo, false)
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
    public List<NoticeResponseDto> getNotices(Pageable pageable) {
        return noticeRepository.findAllByOrderByCreatedDateDesc(pageable)
                .stream()
                .map(NoticeResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<?> getMydataStakings(ServiceUser serviceUser) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        return myStakingDataRepository.findAllByUser(userInfo)
                .stream()
                .map(MyStakingDataResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MyStakingDataDetailResponseDto getMydataStaking(ServiceUser serviceUser, String myStakingDataId, String rewardType) {
        log.info("rewardType : {}", rewardType);
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        MyStakingData myStakingData = myStakingDataRepository.findByMyStakingDataIdAndUser(myStakingDataId, userInfo)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_STAKING_DATA.getCode(), CommonErrorCode.NOT_FOUND_STAKING_DATA.getMessage()));
        LocalDateTime startDate;
        if (rewardType == null) {
            startDate = LocalDateTime.MIN;
        } else {
            switch (rewardType) {
                case "oneWeek" -> startDate = LocalDateTime.now().minusWeeks(1);
                case "oneMonth" -> startDate = LocalDateTime.now().minusMonths(1);
                case "sixMonth" -> startDate = LocalDateTime.now().minusMonths(6);
                default ->
                        throw new CommonException(CommonErrorCode.COMMON_FAIL.getCode(), CommonErrorCode.COMMON_FAIL.getMessage());
            }
        }
        List<MyStakingDataAboutReward> values = myStakingDataAboutRewardRepository.findAllByMyStakingDataAndUserAndUserRegDateAfter(myStakingData, userInfo, startDate.format(DateTimeFormatter.ofPattern("yy.MM.dd")));
        List<MyStakingDataRewardsDto> list = new ArrayList<>();
        values.forEach(value -> {
            list.add(new MyStakingDataRewardsDto(value.getUserRegDate(), value.getTodayCompensationQuantity()));
        });
        return new MyStakingDataDetailResponseDto(myStakingData, list);
    }

    @Transactional
    public void calcStaking(ServiceUser serviceUser, CalcStakingRequestDto calcStakingRequestDto, String myStakingDataId) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        MyStakingData myStakingData = myStakingDataRepository.findByMyStakingDataIdAndUser(myStakingDataId, userInfo)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_STAKING_DATA.getCode(), CommonErrorCode.NOT_FOUND_STAKING_DATA.getMessage()));

        myStakingDataAboutRewardRepository.save(new MyStakingDataAboutReward(calcStakingRequestDto, userInfo, myStakingData));
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
    public void alarmCheck(ServiceUser serviceUser,AlarmCheckRequestDto alarmCheckRequestDto){
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        Alarm alarm = alarmRepository.findByAlarmIdAndUser(alarmCheckRequestDto.getAlarmId(),userInfo)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NULL_DATA.getCode(),CommonErrorCode.NULL_DATA.getMessage()));

        alarm.updateAlarmReadYn();
        alarmRepository.save(alarm);
    }
}
