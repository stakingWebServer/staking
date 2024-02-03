package kr.project.backend.repository.common;

import kr.project.backend.entity.common.CommonGroupFile;
import kr.project.backend.entity.user.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepository extends JpaRepository<Questions,String> {

    boolean existsByCommonGroupFile(CommonGroupFile commonGroupFile);
}
