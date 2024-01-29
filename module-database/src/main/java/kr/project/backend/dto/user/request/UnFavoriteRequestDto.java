package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;



@Data
public class UnFavoriteRequestDto implements Serializable {
    @NotNull
    @Schema(description = "stakingId", example = "2a946a4c-tt66-4818-a09b-5ff111505ab6")
    private String stakingId;
}
///d