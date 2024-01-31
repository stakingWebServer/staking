package kr.project.backend.repository.user;



import kr.project.backend.entity.user.UseClause;
import kr.project.backend.entity.user.User;
import kr.project.backend.entity.user.UserUseClause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserUseClauseRepository extends JpaRepository<UserUseClause, String> {
    

    Optional<UserUseClause> findByUserAndUseClauseAndAgreeYn(User userInfo, UseClause useClause, String agreeYn);

    List<UserUseClause> findAllByUseClauseAndAgreeYn(UseClause useClause, String y);
}
