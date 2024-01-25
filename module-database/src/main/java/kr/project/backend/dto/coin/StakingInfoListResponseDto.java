package kr.project.backend.dto.coin;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.project.backend.entity.coin.StakingInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
public class StakingInfoListResponseDto implements Serializable {
    @Schema(description = "키값",example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private Long stakingId;
    @Schema(description = "코인이름",example = "폴리곤 (MATIC)")
    private String coinName;
    @Schema(description = "연 추정 보상률 (최소)",example = "5.3%")
    private String minAnnualRewardRate;
    @Schema(description = "연 추정 보상률 (최대)",example = "10.3%")
    private String maxAnnualRewardRate;

    public StakingInfoListResponseDto(StakingInfo stakingInfo){
        this.stakingId = stakingInfo.getStakingId();
        this.coinName = stakingInfo.getCoinName();
        this.minAnnualRewardRate = stakingInfo.getMinAnnualRewardRate();
        this.maxAnnualRewardRate = stakingInfo.getMaxAnnualRewardRate();
    }
}
