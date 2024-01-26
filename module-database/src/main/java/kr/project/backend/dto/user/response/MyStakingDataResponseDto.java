package kr.project.backend.dto.user.response;

import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.entity.user.MyStakingData;
import lombok.Data;
import java.io.Serializable;

@Data
public class MyStakingDataResponseDto implements Serializable {
    private String coinName;
    private CoinMarketType coinMarketType;
    private String maxAnnualRewardRate;
    private String totalHoldingsQuantity;
    private String totalCompensationQuantity;

    public MyStakingDataResponseDto(MyStakingData myStakingData) {
        this.coinName = myStakingData.getCoinName();
        this.coinMarketType = myStakingData.getCoinMarketType();
        this.maxAnnualRewardRate = myStakingData.getMaxAnnualRewardRate();
        this.totalHoldingsQuantity = myStakingData.getTotalHoldingsQuantity();
        this.totalCompensationQuantity = myStakingData.getTotalCompensationQuantity();
    }
}
