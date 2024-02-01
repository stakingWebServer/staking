package kr.project.backend.entity.common;

import jakarta.persistence.*;
import kr.project.backend.entity.user.Questions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonGroupFile extends BaseTimeEntity implements Serializable {
    @Id
    @Comment(value = "그룹파일키값")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    private String groupFileId;

    @OneToMany(mappedBy = "commonGroupFile")
    private List<CommonFile> commonFileList;

    @OneToOne(mappedBy = "commonGroupFile")
    private Questions questions;
}
