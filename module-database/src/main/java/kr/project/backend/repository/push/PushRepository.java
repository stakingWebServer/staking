package kr.project.backend.repository.push;

import kr.project.backend.entity.push.Push;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushRepository extends JpaRepository<Push, String> {

    List<Push> findAllByOrderByCreatedDateDesc();
}
