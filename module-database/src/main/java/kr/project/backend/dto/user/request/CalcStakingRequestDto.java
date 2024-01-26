package kr.project.backend.dto.user.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class CalcStakingRequestDto implements Serializable {
    private String userRegDate;
    private String insertAmount;
}
