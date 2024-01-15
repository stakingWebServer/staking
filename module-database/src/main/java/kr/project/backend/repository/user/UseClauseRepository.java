package kr.project.backend.repository.user;


import kr.project.backend.entity.user.UseClause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UseClauseRepository extends JpaRepository<UseClause, UUID> {


    @Query(value = """
            select a.useClauseEssentialYn,a.useClauseTitle,a.useClauseKind 
            from UseClause a 
            left join CommonCode b 
            on b.grpCommonCode = 'USE_CLAUSE_KIND' 
            and a.useClauseKind = b.commonCode 
            left join CommonFile c 
            on a.commonFile.fileId = c.fileId 
            where a.useClauseState = '03'
            """)
    List<UseClause> getUserClauses();

    List<UseClause> findAllByUseClauseState(String useClauseState);
}
