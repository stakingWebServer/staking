package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OwnCoinRequestDto {

    @NotEmpty(message = "스테이킹키값을 입력해주세요.")
    @Schema(description = "스테이킹키값", example = "0a4571d1-8a07-4787-a6d0-e818c794a96b")
    private String stakingId;

    @NotEmpty(message = "보유수량을 입력해주세요.")
    @Schema(description = "보유수량", example = "0.12")
    private String totalHoldings;

}
