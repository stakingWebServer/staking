package kr.project.backend.dto.coin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import kr.project.backend.converter.BooleanToYNConverter;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class StakingInfoDetailResponseDto implements Serializable {
    @Schema(description = "코인이름", example = "폴리곤 (MATIC)")
    private String coinName;

    @Schema(description = "단위", example = "MATIC")
    private String unit;
    @Schema(description = "전일종가", example = "1111")
    private String prevClosingPrice;
    @Schema(description = "연 추정 보상률 (최소)", example = "5.3%")
    private String minAnnualRewardRate;
    @Schema(description = "연 추정 보상률 (최대)", example = "19.3%")
    private String maxAnnualRewardRate;
    @Schema(description = "거래소", example = "업비트")
    private CoinMarketType coinMarketType;
    @Schema(description = "스테이킹/언스테이킹 대기", example = "3시간 / 3일")
    private String stakingStatus;
    @Schema(description = "보상주기", example = "매일")
    private String rewardCycle;
    @Schema(description = "최소신청수량", example = "1.00000000DOT")
    private String minimumOrderQuantity;
    @Schema(description = "검증인 수수료", example = "보상의 10% 공제 후 분배")
    private String verificationFee;

    @Comment(value = "즐겨찾기 유무 'Y' or 'N' ")
    private String favoriteYn;

    @Schema(description = "관련 거래소 리스트", example = "업비트,코인원, etc...")
    private List<AboutCoinMarketDto> coinMarketTypes;

    @Schema(description = "해당 코인의 이율 전체 조회", example = "")
    private List<AboutCoinMaxAnnualRewardRateDto> coinMaxAnnualRewardRates;

    @Schema(description = " 해당 코인들 중 보상률 최대값", example = "")
    private String maxRewardRate;
    @Schema(description = " 해당 코인들 중 보상률 최소값", example = "")
    private String minRewardRate;

    public StakingInfoDetailResponseDto(StakingInfo stakingInfo, List<AboutCoinMarketDto> aboutCoinMarketDtos, List<AboutCoinMaxAnnualRewardRateDto> aboutCoinMaxAnnualRewardRateDtos, String maxRewardRate, String minRewardRate, boolean favoriteCheck) {
        this.coinName = stakingInfo.getCoinName();
        this.unit = stakingInfo.getUnit();
        this.prevClosingPrice = stakingInfo.getPrevClosingPrice();
        this.minAnnualRewardRate = stakingInfo.getMinAnnualRewardRate();
        this.maxAnnualRewardRate = stakingInfo.getMaxAnnualRewardRate();
        this.coinMarketType = stakingInfo.getCoinMarketType();
        this.stakingStatus = stakingInfo.getStakingStatus();
        this.rewardCycle = stakingInfo.getRewardCycle();
        this.minimumOrderQuantity = stakingInfo.getMinimumOrderQuantity();
        this.verificationFee = stakingInfo.getVerificationFee();
        this.coinMarketTypes = aboutCoinMarketDtos;
        this.coinMaxAnnualRewardRates = aboutCoinMaxAnnualRewardRateDtos;
        this.maxRewardRate = maxRewardRate;
        this.minRewardRate = minRewardRate;
        this.favoriteYn = favoriteCheck ? "Y" : "N";
    }
}
