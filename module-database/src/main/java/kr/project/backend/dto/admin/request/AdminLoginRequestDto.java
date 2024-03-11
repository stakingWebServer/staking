package kr.project.backend.dto.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdminLoginRequestDto implements Serializable {
    @Schema(example = "admin")
    private String loginId;//아디
    @Schema(example = "stakingadminuser1234")
    private String password;//비번
}
