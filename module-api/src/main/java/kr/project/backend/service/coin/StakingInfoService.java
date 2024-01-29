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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StakingInfoService {
    private final StakingInfoRepository stakingInfoRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    //@Cacheable(value = "stakingInfoList")
    public StakingInfoAndFavoriteListResponseDto getStakingInfosAll(ServiceUser serviceUser) {
        //회원정보
        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //즐겨찾기 목록 조회
        List<Favorite> myFavorites = favoriteRepository.findAllByUserAndDelYn(userInfo, false);

        List<StakingInfo> stakingInfos = stakingInfoRepository.findAllByCreatedDateBetween(
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        return new StakingInfoAndFavoriteListResponseDto(
                stakingInfos.stream().map(StakingListDto::new).collect(Collectors.toList()),
                myFavorites.stream().map(FavoriteListDto::new).collect(Collectors.toList()));
    }
    @Transactional(readOnly = true)
    public StakingInfoDetailResponseDto getStakingInfo(String stakingId,ServiceUser serviceUser) {

        StakingInfo stakingInfo = stakingInfoRepository.findById(stakingId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(), CommonErrorCode.NOT_FOUND_COIN.getMessage()));

        User userInfo = userRepository.findById(serviceUser.getUserId())
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

        //즐겨찾기 인 스테이킹인지 확인
        boolean favoriteCheck = favoriteRepository.existsByStakingInfoAndUserAndDelYn(stakingInfo,userInfo,false);

        List<StakingInfo> stakingInfos = stakingInfoRepository.findByCoinNameAndCreatedDateBetween(stakingInfo.getCoinName(),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        List<AboutCoinMarketDto> aboutCoinMarketDtos = new ArrayList<>();
        stakingInfos.forEach(aboutCoinMarket -> {
            aboutCoinMarketDtos.add(new AboutCoinMarketDto(aboutCoinMarket.getStakingId(), String.valueOf(aboutCoinMarket.getCoinMarketType())));
        });

        return new StakingInfoDetailResponseDto(stakingInfo, aboutCoinMarketDtos,favoriteCheck);
    }


}

