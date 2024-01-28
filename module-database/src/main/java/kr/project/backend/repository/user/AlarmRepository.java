package kr.project.backend.repository.user;

import kr.project.backend.entity.user.Alarm;
import kr.project.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, String> {

    Page<Alarm> findByUserOrderByCreatedDateDesc(User user, Pageable pageable);

    Optional<Alarm> findByAlarmIdAndUser(String alarmId, User user);

}
