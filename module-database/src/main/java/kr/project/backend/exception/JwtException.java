package kr.project.backend.exception;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {

    private String code;

    public JwtException(String code, String message) {
        super(message);
        this.code = code;
    }

    public JwtException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
