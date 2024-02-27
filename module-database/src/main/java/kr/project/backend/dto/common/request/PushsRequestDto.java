package kr.project.backend.dto.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class PushsRequestDto {
    private String title;
    private String content;
    @Schema(description = "광고성 푸시 여부", example = "N")
    private String advertisementPushYn;
}
