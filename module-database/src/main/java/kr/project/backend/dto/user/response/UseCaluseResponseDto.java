package kr.project.backend.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import kr.project.backend.converter.BooleanToYNConverter;
import kr.project.backend.entity.user.UseClause;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UseCaluseResponseDto implements Serializable {

    @Schema(description = "이용약관ID", example = "2a946a4c-tt66-4818-a09b-5ff111505ab6")
    private String useClauseId;

    @Schema(description = "이용약관 제목", example = "[2024.01.17] 개인정보처리방침 V2.1")
    private String useClauseTitle;

    @Schema(description = "이용약관 필수여부", example = "Y")
    private boolean useClauseEssentialYn;

    @Schema(description = "이용약관 구분", example = "02")
    private String useClauseKind;

    @Schema(description = "이용약관 구분명", example = "개인정보처리방침")
    private String useClauseKindName;

    @Schema(description = "이용약관 url", example = "http://158.179.163.36:8080/useClause/AIndma")
    private String useClauseUrl;

    public UseCaluseResponseDto(UseClause useClause){
        this.useClauseId = useClause.getUseClauseId();
        this.useClauseTitle = useClause.getUseClauseTitle();
        //this.useClauseEssentialYn = useClause.getUseClauseEs
        //this.useClauseKindName = useClause.getUse
    }
}
