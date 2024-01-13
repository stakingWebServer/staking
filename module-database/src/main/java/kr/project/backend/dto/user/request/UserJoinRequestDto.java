package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserJoinRequestDto {

    @NotBlank(message = "이메일을 넣어주세요.")
    @Email
    @Schema(description = "이메일", example = "test@test.com")
    private String userEmail;
    
    @Schema(description = "푸쉬 토큰", example = "asWERds123/sdkmmal2WED/sdmpPalm")
    private String userPushToken;

    @NotBlank(message = "회원가입 sns 구분을 넣어주세요.")
    @Schema(description = "회원가입 sns 구분", example = "01", allowableValues = {"01", "02", "03", "04"})
    private String userJoinSnsKind;

    @NotBlank(message = "회원가입 os 구분을 넣어주세요.")
    @Schema(description = "회원가입 os 구분", example = "01", allowableValues = {"01", "02", "03", "04"})
    private String userJoinOsKind;

    @NotNull(message = "이용약관 리스트를 넣어주세요.")
    private List<UseClauseDto> useClauseDtoList;

}
