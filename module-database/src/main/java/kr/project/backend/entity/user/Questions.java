package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.converter.BooleanToYNConverter;
import kr.project.backend.dto.user.request.QuestionRequestDto;
import kr.project.backend.entity.common.BaseTimeEntity;
import kr.project.backend.entity.common.CommonGroupFile;
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
public class Questions extends BaseTimeEntity implements Serializable {

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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(mappedBy = "questions")
    private Reply reply;


    public Questions(QuestionRequestDto questionRequestDto,CommonGroupFile commonGroupFile,User user){
        this.title = questionRequestDto.getQuestionTitle();
        this.content = questionRequestDto.getQuestionContent();
        this.commonGroupFile = commonGroupFile;
        this.user = user;
    }
}
