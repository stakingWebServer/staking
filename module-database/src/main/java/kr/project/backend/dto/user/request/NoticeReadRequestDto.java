package kr.project.backend.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NoticeReadRequestDto {

    @NotEmpty(message = "공지사항ID를 넣어주세요")
    @Schema(description = "공지사항ID", example = "4f17083a-fd35-47c9-a8e7-8a2bf5c94001")
    private String noticeId;
}
