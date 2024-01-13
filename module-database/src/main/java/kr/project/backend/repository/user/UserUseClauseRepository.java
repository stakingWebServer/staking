package kr.project.database.repository.user;



import kr.project.database.entity.user.UserUseClause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserUseClauseRepository extends JpaRepository<UserUseClause, UUID> {

}
