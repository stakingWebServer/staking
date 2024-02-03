package kr.project.backend.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
@Data
@AllArgsConstructor
public class QuestionFileInfoDto implements Serializable {
    private String fileName;
    private String fileUrl;
}
