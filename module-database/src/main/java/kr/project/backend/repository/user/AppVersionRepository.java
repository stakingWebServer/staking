package kr.project.backend.repository.user;

import kr.project.backend.entity.user.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

}
