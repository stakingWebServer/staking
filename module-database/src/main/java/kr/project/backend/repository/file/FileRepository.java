package kr.project.backend.repository.file;


import kr.project.backend.entity.common.CommonFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<CommonFile, String> {

}
