package kr.project.backend.repository.user;

import kr.project.backend.entity.user.Notice;
import kr.project.backend.entity.user.User;
import kr.project.backend.entity.user.UserNoticeRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNoticeReadRepository extends JpaRepository<UserNoticeRead, String> {

    boolean existsByUserAndNotice(User user, Notice notice);

}
