package kr.project.backend.repository.common;


import kr.project.backend.entity.common.InOutLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InOutLogRepository extends JpaRepository<InOutLog, String> {

}
