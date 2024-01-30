package kr.project.backend.repository.user;


import kr.project.backend.dto.admin.response.PageViewDto;
import kr.project.backend.entity.user.MoveView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoveViewRepository extends JpaRepository<MoveView, Long> {

    @Query(value = "select new kr.project.backend.dto.admin.response.PageViewDto(mv.viewName, count(mv)) from MoveView mv group by mv.viewName")
    List<PageViewDto> getPageViewInfo();
}
