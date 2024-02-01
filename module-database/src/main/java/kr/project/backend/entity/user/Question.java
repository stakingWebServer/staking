package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.dto.user.request.QuestionRequestDto;
import kr.project.backend.entity.common.BaseTimeEntity;
import kr.project.backend.entity.common.CommonFile;
import kr.project.backend.entity.common.CommonGroupFile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "문의ID")
    private String questionId;
    private String title;
    private String content;

    @OneToOne
    @JoinColumn(name = "group_file_id")
    private CommonGroupFile commonGroupFile;

    @OneToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;

    public Question(QuestionRequestDto questionRequestDto){
        this.title = questionRequestDto.getQuestionTitle();
        this.content = questionRequestDto.getQuestionContent();
    }
}
