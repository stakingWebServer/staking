package kr.project.backend.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PageCountRequestDto {

    @NotEmpty(message = "페이지를 넣어주세요.")
    @Schema(description = "페이지", example = "1")
    private int pageCount;

    @NotEmpty(message = "페이지 카운트를 넣어주세요.")
    @Schema(description = "페이지 카운트", example = "10")
    private int pageSize;
}
