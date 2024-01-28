package kr.project.backend.dto.coin;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.project.backend.entity.coin.StakingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StakingInfoAndFavoriteListResponseDto implements Serializable {
    @Schema(description = "메인코인목록",example = "메인코인목록")
    private List<StakingListDto> stakingInfoLists;
    @Schema(description = "즐겨찾기목록",example = "즐겨찾기목록")
    private List<FavoriteListDto> favoriteLists;
}
