package kr.project.backend.repository.push;

import kr.project.backend.entity.push.Push;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushRepository extends JpaRepository<Push, String> {

}