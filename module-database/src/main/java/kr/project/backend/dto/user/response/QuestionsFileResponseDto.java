package kr.project.backend.dto.user.response;

import kr.project.backend.entity.common.CommonFile;
import lombok.Data;

@Data
public class QuestionsFileResponseDto {

    private String fileUrl;

    public QuestionsFileResponseDto(CommonFile commonFile){
        this.fileUrl = commonFile.getFileUrl();
    }
}
