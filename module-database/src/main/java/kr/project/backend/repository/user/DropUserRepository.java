package kr.project.backend.repository.user;


import kr.project.backend.entity.user.DropUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DropUserRepository extends JpaRepository<DropUser, Long> {

    Optional<DropUser> existsByUserEmail(String userEmail);

    Optional<DropUser> findByUserEmail(String userEmail);
}
