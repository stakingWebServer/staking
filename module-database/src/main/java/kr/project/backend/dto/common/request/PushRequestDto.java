package kr.project.backend.dto.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PushRequestDto {

    @NotBlank(message = "유저 이메일을 넣어주세요.")
    @Schema(description = "이메일", example = "test@test.com")
    private String userEmail;

    @NotBlank(message = "제목을 넣어주세요.")
    @Schema(description = "제목", example = "[공지] 알림 안내")
    private String title;

    @NotBlank(message = "내용을 넣어주세요.")
    @Schema(description = "내용", example = "개인 알림 테스트입니다.")
    private String content;

    @NotBlank(message = "알람 구분코드를 넣어주세요.")
    @Schema(description = "알람구분코드", example = "01")
    private String alarmDetailKind;
}
