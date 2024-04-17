package kr.project.backend.repository.user;


import kr.project.backend.dto.admin.response.PageViewDto;
import kr.project.backend.entity.user.MoveView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoveViewRepository extends JpaRepository<MoveView, Long> {


    @Query(value = "SELECT view_name, DATE_FORMAT(created_date, '%Y-%m') AS month, COUNT(view_id) AS view_count " +
            "FROM move_view " +
            "GROUP BY view_name, month " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> getPageViewInfoForMonth();

    @Query(value = "SELECT mv.view_name, DATE_FORMAT(mv.created_date, '%Y-%m-%d') AS day, COUNT(mv.view_id) AS view_count " +
            "FROM move_view mv " +
            "GROUP BY mv.view_name, DATE_FORMAT(mv.created_date, '%Y-%m-%d') " +
            "ORDER BY DATE_FORMAT(mv.created_date, '%Y-%m-%d')", nativeQuery = true)
    List<Object[]> getPageViewInfoForDay();
}
