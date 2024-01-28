package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.Alarm;
import kr.project.backend.entity.user.Notice;
import lombok.Data;

@Data
public class AlarmResponseDto {

    private String alarmId;

    private String alarmTitle;

    private String alarmContent;

    private String alarmDate;

    public AlarmResponseDto(Alarm alarm) {
        this.alarmId = alarm.getAlarmId();
        this.alarmTitle = alarm.getAlarmTitle();
        this.alarmContent = alarm.getAlarmContent();
        this.alarmDate = alarm.getCreatedDate().substring(0,10);
    }
}
