package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppVersionRequestDto implements Serializable {;

    @NotBlank(message = "앱 os 구분을 넣어주세요.")
    @Schema(description = "앱 os 구분", example = "01", allowableValues = {"01", "02", "03", "04"})
    private String appOs;

    @NotBlank(message = "앱 버전을 넣어주세요.")
    @Schema(description = "앱 버전", example = "1.0")
    private String appVersion;

}
