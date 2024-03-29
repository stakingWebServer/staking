package kr.project.backend.dto.user.response;

import kr.project.backend.entity.common.CommonFile;
import kr.project.backend.entity.user.Questions;
import lombok.Data;

import java.util.List;

@Data
public class QuestionsResponseDto {

    private String questionId;
    private String questionDate;
    private String questionTitle;
    private String questionContent;
    private String replyDate;
    private String replyContent;
    private String replyYn = "N";
    private String replyReadYn = "N";
    private String replyId;
    private List<QuestionsFileResponseDto> questionsFileList;


    public QuestionsResponseDto(Questions questions, List<QuestionsFileResponseDto> questionsFileResponseDtoList) {
        this.questionId = questions.getQuestionId();
        this.questionDate = questions.getCreatedDate().substring(0,10);
        this.questionTitle = questions.getTitle();
        this.questionContent = questions.getContent();
        if(questions.getReply() != null){
            this.replyDate = questions.getReply().getCreatedDate().substring(0,10);
            this.replyContent = questions.getReply().getContent();
            this.replyYn = questions.getReply().isReplyYn() ? "Y" : "N";
            this.replyReadYn = questions.getReply().isReplyReadYn() ? "Y" : "N";
            this.replyId = questions.getReply().getReplyId();
        }
        this.questionsFileList = questionsFileResponseDtoList;
    }
}
