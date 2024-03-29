package kr.project.backend.dto.coin;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.project.backend.entity.user.Favorite;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavoriteListDto {
    @Schema(description = "스테이킹 키값", example = "스테이킹 키값")
    private String stakingId;
    @Schema(description = "코인이름", example = "폴리곤 (MATIC)")
    private String coinName;
    @Schema(description = "코인이미지URL", example = "URL")
    private String coinImageUrl;
    @Schema(description = "연 추정 보상률 (최소)", example = "5.3%")
    private String minAnnualRewardRate;
    @Schema(description = "연 추정 보상률 (최대)", example = "10.3%")
    private String maxAnnualRewardRate;

    public FavoriteListDto(Favorite favorite) {
        String numStr = favorite.getStakingInfo().getMaxAnnualRewardRate().replaceAll("[^0-9.]", "");
        this.stakingId = favorite.getStakingInfo().getStakingId();
        this.coinName = favorite.getStakingInfo().getCoinName();
        this.coinImageUrl = favorite.getStakingInfo().getCoinImageUrl() == null ? "" : favorite.getStakingInfo().getCoinImageUrl();
        this.minAnnualRewardRate = favorite.getStakingInfo().getMinAnnualRewardRate();
        this.maxAnnualRewardRate = String.format("%.1f", Double.parseDouble(numStr)).concat("%");
    }
}
