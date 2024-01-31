package kr.project.backend.dto.user.response;

import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.entity.user.Favorite;
import lombok.Data;
import java.io.Serializable;
import java.text.DecimalFormat;

@Data
public class MyStakingDataResponseDto implements Serializable {

    private String stakingId;
    private String coinName;
    private String maxAnnualRewardRate;
    private String totalHoldings;
    private String totalRewards;
    private CoinMarketType coinMarketType;
    private String unit;

    public MyStakingDataResponseDto(Favorite favorite) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##################");
        this.stakingId = favorite.getStakingInfo().getStakingId();
        this.coinName = favorite.getStakingInfo().getCoinName();
        this.maxAnnualRewardRate = favorite.getStakingInfo().getMaxAnnualRewardRate();
        this.totalHoldings = decimalFormat.format(favorite.getTotalHoldings());
        this.totalRewards = decimalFormat.format(favorite.getTotalRewards());
        this.coinMarketType = favorite.getStakingInfo().getCoinMarketType();
        this.unit = favorite.getStakingInfo().getUnit();
    }
}
