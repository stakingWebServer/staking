package kr.project.backend.dto.user.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserRefreshTokenRequestDto implements Serializable {

    @NotNull
    @Schema(description = "refreshTokenId", example = "1")
    private String refreshTokenId;
}