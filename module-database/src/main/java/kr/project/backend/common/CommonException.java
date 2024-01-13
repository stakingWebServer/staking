package kr.project.database.common;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final String code;

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
