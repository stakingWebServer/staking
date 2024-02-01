package kr.project.backend.repository.user;

import kr.project.backend.entity.user.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply,String> {
}
