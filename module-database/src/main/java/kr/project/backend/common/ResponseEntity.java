package kr.project.database.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseEntity<T> implements Serializable {

    @Schema(description = "응답코드",example = "0000")
    private String code;

    @Schema(description = "응답메세지",example = "success")
    private String msg;

    private Object result;
}
