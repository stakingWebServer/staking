package kr.project.backend.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyRequestDto implements Serializable {

    @NotBlank(message = "문의ID를 넣어주세요.")
    private String questionId;

    @NotBlank(message = "답변 내용을 넣어주세요.")
    private String content;
}
