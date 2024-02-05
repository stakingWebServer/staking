package kr.project.backend.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TodayCommonResponseDto implements Serializable {
    private int todayNum;
}
