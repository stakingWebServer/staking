package kr.project.backend.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor

public class TodayDropUserResponseDto implements Serializable {
    private int todayDropUser;
}
