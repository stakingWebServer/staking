package kr.project.backend.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.entity.user.Favorite;
import kr.project.backend.entity.user.MyStakingData;
import lombok.Data;
import java.io.Serializable;

@Data
public class MyStakingDataResponseDto implements Serializable {
    @Schema(description = "즐겨찾기 키값", example = "uuid")
    private String favoriteId;
    @Schema(description = "코인이름", example = "폴리곤 (MATIC)")
    private String coinName;
    @Schema(description = "연 추정 보상률 (최대)", example = "19.3%")
    private String maxAnnualRewardRate;
    @Schema(description = "총 보유수량", example = "0.003")
    private String totalHoldings;
    @Schema(description = "총 보상수량", example = "0.04")
    private String totalRewards;
    @Schema(description = "거래소", example = "upbit")
    private CoinMarketType coinMarketType;


    public MyStakingDataResponseDto(Favorite favorite) {
        this.favoriteId = favorite.getFavoriteId();
        this.coinName = favorite.getStakingInfo().getCoinName();
        this.maxAnnualRewardRate = favorite.getStakingInfo().getMaxAnnualRewardRate();
        this.totalHoldings = String.valueOf(favorite.getTotalHoldings());
        this.totalRewards = String.valueOf(favorite.getTotalRewards());
        this.coinMarketType = favorite.getStakingInfo().getCoinMarketType();

    }
}
