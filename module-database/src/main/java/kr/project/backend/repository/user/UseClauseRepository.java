package kr.project.backend.repository.user;


import kr.project.backend.dto.user.response.UseClauseResponseDto;
import kr.project.backend.entity.user.UseClause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UseClauseRepository extends JpaRepository<UseClause, String> {

    @Query(value = "SELECT new kr.project.backend.dto.user.response.UseClauseResponseDto(" +
                        "A.useClauseId,A.useClauseEssentialYn,C.fileUrl,A.useClauseKind, " +
                        "CASE WHEN A.useClauseEssentialYn = 'Y' THEN CONCAT(B.commonCodeName,'(필수)') " +
                             "ELSE CONCAT(B.commonCodeName,'(선택)') " +
                         "END ," +
                        "CASE WHEN (SELECT COUNT(*) " +
                                     "FROM UseClause UC " +
                                    "WHERE UC.useClauseKind = A.useClauseKind " +
                                      "AND UC.useClauseState = '04') > 0 THEN 'Y' " +
                             "ELSE 'N' " +
                         "END " +
                        ")" +
                   "FROM UseClause A " +
              "LEFT JOIN CommonCode B " +
                     "ON B.grpCommonCode = :useClauseKind " +
                    "AND A.useClauseKind = B.commonCode " +
              "LEFT JOIN CommonFile C " +
                     "ON A.commonFile.fileId = C.fileId " +
                  "WHERE A.useClauseState = :useClauseState ")
    List<UseClauseResponseDto> getUseClauses(@Param(value = "useClauseKind") String useClauseKind, @Param(value = "useClauseState") String useClauseState );

    Optional<UseClause> findByUseClauseKindAndUseClauseState(String advertisementPush, String apply);

    List<UseClause> findByUseClauseKindAndUseClauseStateOrderByCreatedDateDesc(String useClauseKind, String useClauseState);
}
