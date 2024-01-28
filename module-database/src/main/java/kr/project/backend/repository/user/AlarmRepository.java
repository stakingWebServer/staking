package kr.project.backend.repository.user;

import kr.project.backend.entity.user.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, String> {

    Page<Alarm> findAllByOrderByCreatedDateDesc(Pageable pageable);

}
