package kr.project.backend.dto.user.request;

import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.entity.user.Favorite;
import kr.project.backend.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RewadScheduledDto {

    private String userId;
    private String userRegDate;
    private String favoriteId;
    private String myStakingDataAboutRewardId;
    private String stakingId;
    private BigDecimal totalHoldings;
    private BigDecimal totalRewards;
    private CoinMarketType coinMarketType;
    private String coinName;
    private Integer cnt;

    public RewadScheduledDto(String userId,String userRegDate,String favoriteId,String myStakingDataAboutRewardId,String stakingId,BigDecimal totalHoldings,BigDecimal totalRewards,CoinMarketType coinMarketType,String coinName,Integer cnt){
        this.userId = userId;
        this.userRegDate = userRegDate;
        this.favoriteId = favoriteId;
        this.myStakingDataAboutRewardId = myStakingDataAboutRewardId;
        this.stakingId = stakingId;
        this.totalHoldings = totalHoldings;
        this.totalRewards = totalRewards;
        this.coinMarketType = coinMarketType;
        this.coinName = coinName;
        this.cnt = cnt;
    }

}
