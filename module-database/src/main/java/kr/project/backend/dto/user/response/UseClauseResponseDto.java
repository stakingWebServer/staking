package kr.project.backend.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class UseClauseResponseDto {
    private String useClauseId;
    private String useClauseEssentialYn;
    private String useClauseFullTitle;
    private String useClauseBeforeYn;
    private String fileUrl;
    private String useClauseKind;

    public UseClauseResponseDto(String useClauseId, String useClauseEssentialYn, String fileUrl, String useClauseKind, String useClauseFullTitle, String useClauseBeforeYn){
        this.useClauseId = useClauseId;
        this.useClauseEssentialYn = useClauseEssentialYn;
        this.fileUrl = fileUrl;
        this.useClauseKind = useClauseKind;
        this.useClauseFullTitle = useClauseFullTitle;
        this.useClauseBeforeYn = useClauseBeforeYn;
    }

}
