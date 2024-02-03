package kr.project.backend.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StakingsDetailResponseDto {

    private String coinName;
    private String totalHoldings;
    private String totalRewards;
    private String unit;
    private List<MyStakingDataAboutRewardHistoryDto> myStakingDataAboutRewardHistoryDtoList;

}
