package kr.project.backend.repository.common;

import kr.project.backend.entity.user.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,String> {
}
