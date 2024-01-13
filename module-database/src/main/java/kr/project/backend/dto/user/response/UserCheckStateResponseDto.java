package kr.project.backend.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCheckStateResponseDto {

    /** 유저상태 */
    private String userState;

    /** 유저상태명 */
    private String userStateNm;
}
