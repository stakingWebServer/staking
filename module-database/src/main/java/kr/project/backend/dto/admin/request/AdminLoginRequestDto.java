package kr.project.backend.dto.admin.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminLoginRequestDto implements Serializable {
    private String loginId;//아디 
    private String password;//비번
}
