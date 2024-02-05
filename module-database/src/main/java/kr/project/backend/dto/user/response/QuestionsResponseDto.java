package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.Questions;
import lombok.Data;

@Data
public class QuestionsResponseDto {

    private String title;
    private String content;
    private String replyContent;
    private String replyYn = "N";

    public QuestionsResponseDto(Questions questions) {
        this.title = questions.getTitle();
        this.content = questions.getContent();
        if(questions.getReply() != null){
            this.replyContent = questions.getReply().getContent();
            this.replyYn = questions.getReply().isReplyYn() ? "Y" : "N";
        }
    }
}
