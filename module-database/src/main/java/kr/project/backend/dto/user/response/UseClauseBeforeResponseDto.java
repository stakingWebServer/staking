package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.UseClause;
import lombok.Data;

@Data
public class UseClauseBeforeResponseDto {

    private String useClauseTitle;
    private String fileUrl;
    private String createDate;

    public UseClauseBeforeResponseDto(UseClause useClause) {
        this.useClauseTitle = useClause.getUseClauseTitle();
        this.fileUrl = useClause.getCommonFile().getFileUrl();
        this.createDate = useClause.getCreatedDate().substring(0, 10);
    }
}
