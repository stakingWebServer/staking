package kr.project.backend.repository.user;

import kr.project.backend.dto.user.response.NoticeResponseDto;
import kr.project.backend.entity.user.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, String> {

    Page<NoticeResponseDto> findAllByOrderByCreatedDateDesc(Pageable pageable);

}
