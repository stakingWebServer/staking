package kr.project.backend.repository.user;

import kr.project.backend.entity.user.Questions;
import kr.project.backend.entity.user.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,String> {
    boolean existsByQuestions(Questions question);

    Optional<Reply> findByQuestions(Questions question);
}
