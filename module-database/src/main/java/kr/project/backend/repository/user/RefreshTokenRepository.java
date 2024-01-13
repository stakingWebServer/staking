package kr.project.database.repository.user;


import kr.project.database.entity.user.RefreshToken;
import kr.project.database.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUser(User userInfo);

    Optional<RefreshToken> findByRefreshTokenId(UUID refreshTokenId);
}
