package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MoveViewRequestDto {

    @NotEmpty(message = "화면명을 넣어주세요")
    @Schema(description = "화면명", example = "메인")
    private String viewName;

}
