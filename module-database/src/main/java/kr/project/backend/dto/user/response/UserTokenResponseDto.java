package kr.project.database.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenResponseDto {

    /** accessToken */
    @Schema(description = "accessToken",example = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJhZGI1Y2M5Yy01NDVmLTRlMGItOGRjYy1iMzhkNTdlZTZkYjkiLCJtbWJyTm0iOiLquYDtmY3roYAiLCJ1c2VyRW1haWwiOiJ0ZXN0MjJAbWFpbC5jb20iLCJpYXQiOjE3MDQxMzA0MDAsImV4cCI6MTcwNDEzMDcwMH0.3zL8l4pmqg4sttKgTUGDVl1EpwCtvnIiaAon2VdvUMI")
    private String accessToken;

    /** refreshTokenId */
    @Schema(description = "refreshTokenId",example = "34be386f-37f6-4fae-806a-e463d5a90315")
    private String refreshTokenId;


}
