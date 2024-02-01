package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequestDto {

    @NotEmpty(message = "문의사항 제목을 입력해주세요.")
    @Schema(description = "문의사항 제목", example = "보상수량 문의")
    private String questionTitle;

    @NotEmpty(message = "문의사항 내용을 입력해주세요.")
    @Schema(description = "문의사항 내용", example = "업비트 보상주기는 어디서 봐야 하나요?")
    private String questionContent;

    @Schema(description = "파일ID", example = "SFMEAQ19QA")
    private List<file> fileList;

    @lombok.Data
    public static class file {
        private String fileId;
    }
}
