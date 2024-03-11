package kr.project.backend.common;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.project.backend.results.ApiResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseMessage {
    @Schema(description = "성공 여부", example = "SUCCESS")
    private String status;
    @Schema(description = "처리 메시지", example = "정상처리")
    private String message;
    @Schema(description = "에러 코드", example = "null")
    private String errorCode;
    @Schema(description = "에러 메시지", example = "null")
    private String errorMessage;
    @Schema(description = "결과")
    private Object result;

    public ApiResponseMessage(String status, String message, String errorCode, String errorMessage){
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
///ㅇ
    public ApiResponseMessage(ApiResult result){
        this.status = CommonErrorCode.SUCCESS.getCode();
        this.message = CommonErrorCode.SUCCESS.getMessage();
        this.result = result;
    }

    public ApiResponseMessage(List<?> result){
        this.status = CommonErrorCode.SUCCESS.getCode();
        this.message = CommonErrorCode.SUCCESS.getMessage();
        this.result = result;
    }
    public void setError(CommonErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }

    public ApiResponseMessage(Page<?> result){
        this.status = CommonErrorCode.SUCCESS.getCode();
        this.message = CommonErrorCode.SUCCESS.getMessage();
        this.result = result;
    }
}
