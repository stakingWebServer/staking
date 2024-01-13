package kr.project.database.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.project.database.entity.coin.enumType.CoinMarketType;
import kr.project.database.entity.user.Favorite;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FavoriteResponseDto implements Serializable {
    @Schema(description = "코인이름",example = "폴리곤 (MATIC)")
    private String coinName;
    @Schema(description = "연 추정 보상률 (최대)",example = "19.3%")
    private String maxAnnualRewardRate;
    @Schema(description = "총 보유수량",example = "0.003")
    private String totalHoldings;
    @Schema(description = "총 보상수량",example = "0.04")
    private String totalRewards;
    @Schema(description = "거래소",example = "upbit")
    private CoinMarketType coinMarketType;

    public FavoriteResponseDto(Favorite favorite){
        this.coinName = favorite.getStakingInfo().getCoinName();
        this.maxAnnualRewardRate = favorite.getStakingInfo().getMaxAnnualRewardRate();
        this.totalHoldings = String.valueOf(favorite.getTotalHoldings());
        this.totalRewards = String.valueOf(favorite.getTotalRewards());
        this.coinMarketType = favorite.getStakingInfo().getCoinMarketType();
    }

}
