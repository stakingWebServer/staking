package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserAlarmSetRequestDto {

    @NotEmpty(message = "알림구분을 넣어주세요")
    @Schema(description = "알림구분", example = "01")
    private String alarmKind;
}
