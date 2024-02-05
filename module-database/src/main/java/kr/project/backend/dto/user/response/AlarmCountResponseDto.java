package kr.project.backend.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmCountResponseDto {

    private String userEmail;
    private String newAlarm;
    private long alarmCount;

}
