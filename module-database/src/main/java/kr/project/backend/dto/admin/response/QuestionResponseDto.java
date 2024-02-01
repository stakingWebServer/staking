package kr.project.backend.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class QuestionResponseDto {
    private String title;
    private String content;
    private List<QuestionFileInfoDto> fileInfos;

}
