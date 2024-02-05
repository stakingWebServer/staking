package kr.project.backend.entity.user;


import jakarta.persistence.*;
import kr.project.backend.converter.BooleanToYNConverter;
import kr.project.backend.dto.admin.request.ReplyRequestDto;
import kr.project.backend.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "댓글키값")
    private String replyId;
    private String content;
    @Column(columnDefinition = "VARCHAR(1) default 'Y'")
    @Convert(converter = BooleanToYNConverter.class)
    @Comment(value = "답변 유무")
    private boolean replyYn;
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    @Convert(converter = BooleanToYNConverter.class)
    @Comment(value = "답변 읽음 유무")
    private boolean replyReadYn;
    @OneToOne
    @JoinColumn(name = "question_id")
    private Questions questions;


    public Reply(ReplyRequestDto replyRequestDto){
        this.content = replyRequestDto.getContent();
    }
}
