package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.UserAlarmSet;
import lombok.Data;

@Data
public class AlarmSetResponseDto {

    private String alarmKind;
    private String alarmSetYn;

    public AlarmSetResponseDto(UserAlarmSet userAlarmSet){
        this.alarmKind = userAlarmSet.getAlarmKind();
        this.alarmSetYn = userAlarmSet.getAlarmSetYn();
    }

    public AlarmSetResponseDto(String alarmKind, String alarmSetYn){
        this.alarmKind = alarmKind;
        this.alarmSetYn = alarmSetYn;
    }

}
