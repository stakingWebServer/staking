package kr.project.backend.service.coin;

import kr.project.backend.common.CommonErrorCode;
import kr.project.backend.common.CommonException;
import kr.project.backend.dto.coin.AboutCoinMarketDto;
import kr.project.backend.dto.coin.StakingInfoDetailResponseDto;
import kr.project.backend.dto.coin.StakingInfoListResponseDto;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.repository.coin.StakingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StakingInfoService {
    private final StakingInfoRepository stakingInfoRepository;

    //@Cacheable(value = "stakingInfoList")
    @Transactional(readOnly = true)
    public List<StakingInfoListResponseDto> getStakingInfos() {
        return stakingInfoRepository.findAll().stream().map(StakingInfoListResponseDto::new).collect(Collectors.toList());
        /*return stakingInfoRepository.findAllByCreatedDateBetween(
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .stream()
                .map(StakingInfoListResponseDto::new)
                .collect(Collectors.toList());*/
    }

    @Transactional(readOnly = true)
    public StakingInfoDetailResponseDto getStakingInfo(UUID stakingId) {
        StakingInfo stakingInfo = stakingInfoRepository.findById(stakingId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_COIN.getCode(), CommonErrorCode.NOT_FOUND_COIN.getMessage()));

        List<StakingInfo> stakingInfos = stakingInfoRepository.findByCoinNameAndCreatedDateBetween(stakingInfo.getCoinName(),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        List<AboutCoinMarketDto> aboutCoinMarketDtos = new ArrayList<>();
        stakingInfos.forEach(aboutCoinMarket -> {
            aboutCoinMarketDtos.add(new AboutCoinMarketDto(aboutCoinMarket.getStakingId(), String.valueOf(aboutCoinMarket.getCoinMarketType())));
        });

        return new StakingInfoDetailResponseDto(stakingInfo, aboutCoinMarketDtos);
    }
}

