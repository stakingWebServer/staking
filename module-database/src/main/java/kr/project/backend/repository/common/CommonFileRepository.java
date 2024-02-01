package kr.project.backend.repository.common;

import kr.project.backend.entity.common.CommonFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CommonFileRepository extends JpaRepository<CommonFile, String> {

    Optional<CommonFile> findByFileId(String fileId);

}
