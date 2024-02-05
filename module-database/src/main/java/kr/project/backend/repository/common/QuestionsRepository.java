package kr.project.backend.repository.common;

import kr.project.backend.entity.common.CommonGroupFile;
import kr.project.backend.entity.user.Questions;
import kr.project.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuestionsRepository extends JpaRepository<Questions,String> {

    boolean existsByCommonGroupFile(CommonGroupFile commonGroupFile);

    Page<Questions> findByUserOrderByCreatedDateDesc(User user, Pageable pageable);
}
