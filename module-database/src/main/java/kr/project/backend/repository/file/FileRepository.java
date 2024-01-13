package kr.project.database.repository.file;


import kr.project.database.common.CommonFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<CommonFile, String> {

}
