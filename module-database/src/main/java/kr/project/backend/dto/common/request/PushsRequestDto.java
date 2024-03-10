package kr.project.backend.dto.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class PushsRequestDto {

    @NotBlank(message = "제목을 넣어주세요.")
    @Schema(description = "제목", example = "[공지] 긴급 점검 안내")
    private String title;

    @NotBlank(message = "내용을 넣어주세요.")
    @Schema(description = "내용", example = "금일 23시부터 서버점검이 예정되어있습니다.")
    private String content;

    @NotBlank(message = "광고성 푸시 여부를 넣어주세요.")
    @Schema(description = "광고성 푸시 여부", example = "N")
    private String advertisementPushYn;

    @NotBlank(message = "알람 구분코드를 넣어주세요.")
    @Schema(description = "알람구분코드", example = "01")
    private String alarmDetailKind;
}
