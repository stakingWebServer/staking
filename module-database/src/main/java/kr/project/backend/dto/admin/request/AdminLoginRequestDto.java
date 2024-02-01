package kr.project.backend.dto.admin.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminLoginRequestDto implements Serializable {
    private String loginId;
    private String password;
}
