package kr.project.backend.repository.common;

import kr.project.backend.entity.common.CommonFile;
import kr.project.backend.entity.common.CommonGroupFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface CommonFileRepository extends JpaRepository<CommonFile, String> {

    Optional<CommonFile> findByFileId(String fileId);

    List<CommonFile> findByCommonGroupFileOrderByCreatedDate(CommonGroupFile commonGroupFile);

}
