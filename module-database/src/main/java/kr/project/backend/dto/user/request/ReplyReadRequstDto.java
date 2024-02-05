package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ReplyReadRequstDto {

    @NotEmpty(message = "답변ID를 넣어주세요")
    @Schema(description = "답변ID", example = "50ce80da-ae98-4562-9297-1c66c8045bab")
    private String replyId;
}
