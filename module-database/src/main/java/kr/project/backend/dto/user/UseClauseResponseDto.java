package kr.project.backend.dto.user;

import lombok.Data;

@Data
public class UseClauseResponseDto {
    private boolean useClauseEssentialYn;
    private String useClauseTitle;
    private String useClauseKind;
    private String commonCodeName;
    private String fileUrl;
}
