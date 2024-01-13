package kr.project.database.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Data
public class AddFavoriteRequestDto implements Serializable {
    @NotNull
    @Schema(description = "stakingId", example = "9854112a-f2a9-4cde-86b5-d54569db7120")
    private UUID stakingId;
}
