package kr.project.backend.repository.user;



import kr.project.backend.entity.user.UserUseClause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserUseClauseRepository extends JpaRepository<UserUseClause, String> {

}
