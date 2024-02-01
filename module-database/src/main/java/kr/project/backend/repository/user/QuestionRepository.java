package kr.project.backend.repository.user;

import kr.project.backend.entity.user.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Questions,String> {
}
