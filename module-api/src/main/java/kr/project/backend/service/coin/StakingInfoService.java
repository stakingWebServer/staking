package kr.project.backend.service.coin;

import kr.project.backend.auth.ServiceUser;
import kr.project.backend.common.CommonErrorCode;
import kr.project.backend.common.CommonException;
import kr.project.backend.dto.coin.*;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.user.Favorite;
import kr.project.backend.entity.user.User;
import kr.project.backend.repository.coin.StakingInfoRepository;
import kr.project.backend.repository.user.FavoriteRepository;
import kr.project.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StakingInfoService {
    private final StakingInfoRepository stakingInfoRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    @Cacheable(value = "stakingInfoList",key = "#serviceUser.userId")
    public StakingInfoAndFavoriteListResponseDto getStakingInfosAll(ServiceUser serviceUser) {
        //날짜 조건식
        LocalDate today = LocalDate.now();
        LocalDateTime startDate = today.atStartOfDay();
        LocalDateTime endDate = today.atTime(23, 59, 59);
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //스테이킹 목록, 즐겨찾기 목록 조회
        List<StakingInfo> stakingInfos = stakingInfoRepository.getStakingInfos(startDate, endDate);
        List<Favorite> myFavorites = favoriteRepository.findAllByUserAndDelYnOrderByStakingInfoMaxAnnualRewardRateNumericDesc(userInfo, false);

        return new StakingInfoAndFavoriteListResponseDto(
                stakingInfos.stream().map(StakingListDto::new).collect(Collectors.toList()),
                myFavorites.stream().map(FavoriteListDto::new).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public StakingInfoDetailResponseDto getStakingInfo(String stakingId, ServiceUser serviceUser) {

        StakingInfo stakingInfo = stakingInfoRepository.findById(stakingId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(), CommonErrorCode.NOT_FOUND_COIN.getMessage()));

        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //즐겨찾기 인 스테이킹인지 확인
        boolean favoriteCheck = favoriteRepository.existsByStakingInfoAndUserAndDelYn(stakingInfo, userInfo, false);

        List<StakingInfo> stakingInfos = stakingInfoRepository.findByCoinNameAndCreatedDateBetween(
                stakingInfo.getCoinName(),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        List<AboutCoinMarketDto> aboutCoinMarketDtos = new ArrayList<>();
        List<AboutCoinMaxAnnualRewardRateDto> aboutCoinMaxAnnualRewardRateDtos = new ArrayList<>();
        //각 거래소마다 있는 동일 코인들의 최대값과 최소값 구하는 식.
        StakingInfo maxRewardRate = Collections.max(stakingInfos, Comparator.comparing(StakingInfo::getMaxAnnualRewardRateNumeric));
        StakingInfo minRewardRate = Collections.min(stakingInfos, Comparator.comparing(StakingInfo::getMaxAnnualRewardRateNumeric));

        stakingInfos.forEach(aboutCoinMarket -> {
            aboutCoinMarketDtos.add(new AboutCoinMarketDto(aboutCoinMarket.getStakingId(), String.valueOf(aboutCoinMarket.getCoinMarketType())));
            aboutCoinMaxAnnualRewardRateDtos.add(new AboutCoinMaxAnnualRewardRateDto(aboutCoinMarket.getCoinMarketType(), aboutCoinMarket.getMaxAnnualRewardRate()));

        });

        return new StakingInfoDetailResponseDto(stakingInfo, aboutCoinMarketDtos, aboutCoinMaxAnnualRewardRateDtos, maxRewardRate.getMaxAnnualRewardRate(), minRewardRate.getMaxAnnualRewardRate(), favoriteCheck);
    }


}

