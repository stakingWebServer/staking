package kr.project.database.dto.coin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AboutCoinMarketDto implements Serializable {
    private UUID stakingId;
    private String aboutCoinMarketType;
}
