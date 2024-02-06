package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.UseClause;
import lombok.Data;

@Data
public class UseClauseBeforeResponseDto {

    private String useClauseKind;
    private String useClauseTitle;
    private String fileUrl;
    private String createDate;

    public UseClauseBeforeResponseDto(UseClause useClause) {
        this.useClauseKind = useClause.getUseClauseKind();
        this.useClauseTitle = useClause.getUseClauseTitle();
        this.fileUrl = useClause.getCommonFile().getFileUrl();
        this.createDate = useClause.getCreatedDate().substring(0, 10);
    }
}
