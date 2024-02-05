package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.Questions;
import lombok.Data;

@Data
public class QuestionsResponseDto {

    private String questionDate;
    private String questionTitle;
    private String questionContent;
    private String replyDate;
    private String replyContent;
    private String replyYn = "N";
    private String replyReadYn = "N";
    private String replyId;


    public QuestionsResponseDto(Questions questions) {
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
    }
}
