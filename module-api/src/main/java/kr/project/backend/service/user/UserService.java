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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    @Transactional
    public UserTokenResponseDto userLogin(UserLoginRequestDto userLoginRequestDto) {

        //등록되어 있는 유저인지 아닌지 판단
        boolean checkUserInfo = userRepository.existsByUserEmail(userLoginRequestDto.getUserEmail());

        //등록되어 있지 않는 유저
        if (!checkUserInfo) {

            //회원가입 1달 제한 정책 체크
            DropUser dropCheck = dropUserRepository.findByUserEmail(userLoginRequestDto.getUserEmail()).orElse(null);

            if (dropCheck != null) {
                try {
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dropDate = transFormat.parse(dropCheck.getDropDttm());
                    Date nowDate = transFormat.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                    Long dropAndJoinTerm = (nowDate.getTime() - dropDate.getTime()) / 1000 / (24 * 60 * 60);

                    if (reJoinTermDate > dropAndJoinTerm.intValue()) {
                        throw new CommonException(CommonErrorCode.JOIN_TERM_DATE.getCode(), CommonErrorCode.JOIN_TERM_DATE.getMessage());
                    }
                } catch (ParseException e) {
                    log.info("date 변환 파싱 error");
                }
            }

            //회원가입 안내 return
            throw new CommonException(CommonErrorCode.JOIN_TERM_DATE.getCode(),CommonErrorCode.JOIN_TERM_DATE.getMessage());
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
    public UserTokenResponseDto userJoin(UserJoinRequestDto userJoinRequestDto){

        //등록되어 있는 유저인지 아닌지 판단
        boolean checkUserInfo = userRepository.existsByUserEmail(userJoinRequestDto.getUserEmail());

        if(checkUserInfo){
            throw new CommonException(CommonErrorCode.ALREADY_JOIN_USER.getCode(),CommonErrorCode.ALREADY_JOIN_USER.getMessage());
        }

        //회원가입
        UUID userId = userRepository.save(new User(userJoinRequestDto)).getUserId();

        //방금 회원가입 된 유저 정보 가져오기
        User userInfo = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //이용약관 동의 저장
        for(int i = 0; i<userJoinRequestDto.getUseClauseDtoList().size(); i++ ){
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
        User userInfo = userRepository.findById(UUID.fromString(serviceUser.getUserId()))
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //로그아웃시간 업데이트
        userInfo.updateUserLogoutDttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        userRepository.save(userInfo);
    }

    @Transactional
    public void dropUser(ServiceUser serviceUser) {

        //회원정보
        User userInfo = userRepository.findById(UUID.fromString(serviceUser.getUserId()))
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
        User userInfo = userRepository.findById(UUID.fromString(serviceUser.getUserId()))
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        CommonCode commonCode = commonCodeRepository.findByGrpCommonCodeAndCommonCode(Constants.USER_STATE.CODE, userInfo.getUserState())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NULL_DATA.getCode(), CommonErrorCode.NULL_DATA.getMessage()));
        ;

        return new UserCheckStateResponseDto(userInfo.getUserState(), commonCode.getCommonCodeName());
    }

    @Transactional
    public AddFavoriteResponseDto addFavorite(ServiceUser serviceUser, AddFavoriteRequestDto addFavoriteRequestDto) {
        //회원정보
        User userInfo = userRepository.findById(UUID.fromString(serviceUser.getUserId()))
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));
        //코인정보
        StakingInfo stakingInfo = stakingInfoRepository.findById(addFavoriteRequestDto.getStakingId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(), CommonErrorCode.NOT_FOUND_COIN.getMessage()));

        //데이터 중복 방지 코드
        boolean favorite = favoriteRepository.existsByStakingInfoAndUserAndDelYn(stakingInfo,userInfo,false);
        if(favorite){
            throw new CommonException(CommonErrorCode.ALREADY_EXIST_FAVORITE.getCode(), CommonErrorCode.ALREADY_EXIST_FAVORITE.getMessage());
        }

        Favorite favoriteInfo = favoriteRepository.save(new Favorite(userInfo, stakingInfo));
        //favorite 키값 반환
        return new AddFavoriteResponseDto(favoriteInfo.getFavoriteId());
    }

    @Transactional
    public void unFavorite(ServiceUser serviceUser, UnFavoriteRequestDto unFavoriteRequestDto) {
        //회원정보
        User userInfo = userRepository.findById(UUID.fromString(serviceUser.getUserId()))
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //즐겨찾기 정보
        Favorite favorite = favoriteRepository.findByFavoriteIdAndUserAndDelYn(unFavoriteRequestDto.getFavoriteId(),userInfo,false)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_FAVORITE.getCode(), CommonErrorCode.NOT_FOUND_FAVORITE.getMessage()));

        //즐겨찾기 헤제.
        favorite.unFavorite();

    }

    @Transactional(readOnly = true)
    public List<FavoriteResponseDto> getFavorites(ServiceUser serviceUser) {
        //회원정보
        User userInfo = userRepository.findById(UUID.fromString(serviceUser.getUserId()))
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        return favoriteRepository.findAllByUserAndDelYn(userInfo,false)
                .stream()
                .map(FavoriteResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UseClauseResponseDto> getUseClauses() {
        //이용약관 목록조회
        return useClauseRepository.getUserClauses(Constants.USE_CLAUSE_KIND.CODE,Constants.USE_CLAUSE_STATE.APPLY);
    }

    @Transactional(readOnly = true)
    public AppVersionResponseDto getAppVersion(String appOs, String appVersion){
        //강제업데이트 조회
        AppVersion appVersionData = appVersionRepository.findByAppOsAndMinimumVersionGreaterThanAndHardUpdateYn(appOs,appVersion, true).orElse(null);

        AppVersionResponseDto appVersionResponseDto = new AppVersionResponseDto();

        if(appVersionData == null){
            appVersionResponseDto.setHardUpdateYn(Constants.YN.N);
        }else{
            appVersionResponseDto.setHardUpdateYn(Constants.YN.Y);
        }
        return appVersionResponseDto;
    }
}
