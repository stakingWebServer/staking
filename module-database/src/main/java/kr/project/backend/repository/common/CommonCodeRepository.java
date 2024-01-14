package kr.project.backend.repository.common;


import kr.project.backend.entity.common.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {

    Optional<CommonCode> findByGrpCommonCodeAndCommonCode(String grpCommonCode, String commonCode);
}
