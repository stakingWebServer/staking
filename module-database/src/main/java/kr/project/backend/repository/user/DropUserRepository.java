package kr.project.backend.repository.user;


import kr.project.backend.entity.user.DropUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DropUserRepository extends JpaRepository<DropUser, String> {

    Optional<DropUser> existsByUserEmail(String userEmail);

    @Query(value = """
        select m.dropDttm
          from DropUser m
         where m.userEmail = :userEmail
      order by m.dropDttm desc
            """)
    Page<DropUser> findByUserEmail(@Param(value = "userEmail") String userEmail, Pageable pageable);

    int countByDropDttmBetween(String startDate, String endDate);
}
