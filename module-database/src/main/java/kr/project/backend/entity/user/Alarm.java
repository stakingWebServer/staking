package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.converter.BooleanToYNConverter;
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
public class Alarm extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "알람ID")
    private String alarmId;

    @Comment(value = "알람 제목")
    @Column(length = 100)
    private String alarmTitle;

    @Comment(value = "알람 내용")
    @Column(columnDefinition = "TEXT")
    private String alarmContent;

    @Comment(value = "읽음 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String alarmReadYn;

    @Comment(value = "알람 디테일 구분")
    @Column(length = 2)
    private String alarmDetailKind;

    @Comment(value = "알람 디테일 구분 ID 값")
    @Column(length = 38)
    private String alarmDetailId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Alarm(String alarmTitle,String alarmContent,User user){
        this.alarmTitle = alarmTitle;
        this.alarmContent = alarmContent;
        this.user = user;
    }

    public Alarm(String alarmTitle,String alarmContent,User user,String alarmDetailKind, String alarmDetailId){
        this.alarmTitle = alarmTitle;
        this.alarmContent = alarmContent;
        this.user = user;
        this.alarmDetailKind = alarmDetailKind;
        this.alarmDetailId = alarmDetailId;
    }

    public void updateAlarmReadYn(){
        this.alarmReadYn = "Y";
    }

}
