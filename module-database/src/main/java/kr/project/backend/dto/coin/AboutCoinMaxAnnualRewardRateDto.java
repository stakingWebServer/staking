package kr.project.backend.dto.coin;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AboutCoinMaxAnnualRewardRateDto implements Serializable {
    private CoinMarketType coinMarketType;
    @Schema(description = "연 추정 보상률 (최대)",example = "19.3%")
    private String maxAnnualRewardRate;
}
