package kr.project.database.repository.user;


import kr.project.database.entity.user.DropUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DropUserRepository extends JpaRepository<DropUser, UUID> {

    Optional<DropUser> existsByUserEmail(String userEmail);

    Optional<DropUser> findByUserEmail(String userEmail);
}
