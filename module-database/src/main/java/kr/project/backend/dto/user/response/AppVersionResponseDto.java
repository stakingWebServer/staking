package kr.project.backend.dto.user.response;

import lombok.Data;

@Data
public class AppVersionResponseDto {

    /** 강제 업데이트 여부 */
    private String hardUpdateYn;
}
