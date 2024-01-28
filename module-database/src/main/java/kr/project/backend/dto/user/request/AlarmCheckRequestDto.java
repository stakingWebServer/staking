package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AlarmCheckRequestDto {

    @NotBlank(message = "알림ID를 넣어주세요")
    @Schema(description = "알림ID", example = "4f17083a-fd35-47c9-a8e7-aaabf5cbb001")
    private String alarmId;

}
