package kr.project.backend.repository.user;


import kr.project.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);

    int countByCreatedDateBetween(String startDate, String endDate);

    int countByUserLoginDttmBetween(String startDate, String endDate);
}
