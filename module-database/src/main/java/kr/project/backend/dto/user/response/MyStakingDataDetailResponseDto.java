package kr.project.backend.dto.user.response;

import kr.project.backend.entity.coin.enumType.CoinMarketType;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class MyStakingDataDetailResponseDto implements Serializable {
    private String coinName;
    private CoinMarketType coinMarketType;
    private String totalHoldingsQuantity;
    private String totalCompensationQuantity;
    private List<MyStakingDataRewardsDto> rewards;
/*
    public MyStakingDataDetailResponseDto(Favorite favorite, List<MyStakingDataRewardsDto> list){
        this.coinName = favorite.getStakingInfo().getCoinName();
        this.coinMarketType = favorite.getStakingInfo().getCoinMarketType();
        this.totalHoldingsQuantity =
        this.totalCompensationQuantity = myStakingData.getTotalCompensationQuantity();
        this.rewards = list;
    }*/
}
