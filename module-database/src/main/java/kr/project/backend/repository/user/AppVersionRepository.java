package kr.project.backend.repository.user;

import kr.project.backend.entity.user.AppVersion;
import kr.project.backend.entity.user.Favorite;
import kr.project.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    Optional<AppVersion> findByAppOsAndMinimumVersionGreaterThanAndHardUpdateYnTrue(String appOs, String appVersion);
}
