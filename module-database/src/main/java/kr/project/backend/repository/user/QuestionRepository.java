package kr.project.backend.repository.user;

import kr.project.backend.entity.user.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Questions,String> {

    List<Questions> findAllByOrderByCreatedDateDesc();
}
