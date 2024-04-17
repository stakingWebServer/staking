package kr.project.backend.entity.push;

import jakarta.persistence.*;
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
public class Push extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "푸시ID")
    private String pushId;

    @Comment(value = "푸시 제목")
    @Column(length = 100)
    private String pushTitle;

    @Comment(value = "푸시 내용")
    private String pushContent;


    public Push(String pushTitle, String pushContent) {
        this.pushTitle = pushTitle;
        this.pushContent = pushContent;
    }
}
