package kr.project.backend.repository.user;



import kr.project.backend.entity.user.UseClause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UseClauseRepository extends JpaRepository<UseClause, UUID> {

    List<UseClause> findAllByUseClauseState(String useClauseState);
}
