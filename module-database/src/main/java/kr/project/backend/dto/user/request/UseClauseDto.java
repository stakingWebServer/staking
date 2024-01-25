package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;

@Data
public class UseClauseDto {

    @NotEmpty(message = "이용약관ID를 넣어주세요")
    @Schema(description = "이용약관ID", example = "1")
    private String useClauseId;

    @NotEmpty(message = "이용약관 동의여부")
    @Schema(description = "이용약관 동의여부", example = "Y")
    private String useClauseAgreeYN;
}
