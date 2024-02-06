package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.dto.user.request.UserAlarmSetRequestDto;
import kr.project.backend.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class UserAlarmSet extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "유저알람세팅ID")
    private String userAlarmSetId;

    @Comment(value = "알람 구분")
    @Column(length = 2)
    private String alarmKind;

    @Comment(value = "알람 설정 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String alarmSetYn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserAlarmSet(UserAlarmSetRequestDto userAlarmSetRequestDto, User user){
        this.alarmKind = userAlarmSetRequestDto.getAlarmKind();
        this.alarmSetYn = userAlarmSetRequestDto.getAgreeYn();
        this.user = user;
    }

    public void updateAlarmSetYn(String alarmSetYn){
        this.alarmSetYn = alarmSetYn;
    }
}
