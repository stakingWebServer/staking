package kr.project.backend.repository.user;

import kr.project.backend.entity.user.User;
import kr.project.backend.entity.user.UserAlarmSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAlarmSetRepository extends JpaRepository<UserAlarmSet, String> {

    Optional<UserAlarmSet> findByUserAndAlarmKind(User user, String alarmKind);

    Optional<UserAlarmSet> findByUser(User user);

    Optional<UserAlarmSet> findByUserAndAlarmKindAndAlarmSetYn(User user, String appInPush, String y);
}
