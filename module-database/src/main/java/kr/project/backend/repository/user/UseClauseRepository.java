package kr.project.backend.repository.user;


import kr.project.backend.dto.user.response.UseClauseResponseDto;
import kr.project.backend.entity.user.UseClause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UseClauseRepository extends JpaRepository<UseClause, String> {


    @Query(value = """
            select a.useClauseEssentialYn,a.useClauseTitle,a.useClauseKind,b.commonCodeName,c.fileUrl
            from UseClause a 
            left join CommonCode b 
            on b.grpCommonCode = :useClauseKind
            and a.useClauseKind = b.commonCode 
            left join CommonFile c 
            on a.commonFile.fileId = c.fileId 
            where a.useClauseState = :useClauseState
            """)
    List<UseClauseResponseDto> getUserClauses(@Param(value = "useClauseKind") String useClauseKind,
                                              @Param(value = "useClauseState") String useClauseState);

}
