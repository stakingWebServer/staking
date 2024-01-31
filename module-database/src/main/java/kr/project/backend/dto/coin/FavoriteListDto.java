package kr.project.backend.dto.coin;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.project.backend.entity.user.Favorite;
import lombok.Data;

@Data
public class FavoriteListDto {
    @Schema(description = "스테이킹 키값",example = "스테이킹 키값")
    private String stakingId;
    @Schema(description = "코인이름",example = "폴리곤 (MATIC)")
    private String coinName;
    @Schema(description = "연 추정 보상률 (최소)",example = "5.3%")
    private String minAnnualRewardRate;
    @Schema(description = "연 추정 보상률 (최대)",example = "10.3%")
    private String maxAnnualRewardRate;
    public FavoriteListDto(Favorite favorite){
        this.stakingId = favorite.getStakingInfo().getStakingId();
        this.coinName = favorite.getStakingInfo().getCoinName();
        this.minAnnualRewardRate = favorite.getStakingInfo().getMinAnnualRewardRate();
        this.maxAnnualRewardRate = favorite.getStakingInfo().getMaxAnnualRewardRate();
    }
}