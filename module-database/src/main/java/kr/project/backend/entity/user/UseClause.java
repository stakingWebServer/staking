package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.entity.common.BaseTimeEntity;
import kr.project.backend.entity.common.CommonFile;
import kr.project.backend.converter.BooleanToYNConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UseClause extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "이용약관 키값")
    private String useClauseId;

    @Comment(value = "이용약관 제목")
    @Column(length = 50)
    private String useClauseTitle;

    @Comment(value = "이용약관 필수여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String useClauseEssentialYn;

    @Comment(value = "이용약관 구분")
    @Column(length = 2)
    private String useClauseKind; //

    @Comment(value = "이용약관 상태")
    @Column(length = 2)
    private String useClauseState;

    @OneToMany(mappedBy = "useClause")
    private List<UserUseClause> userUseClauses;

    @OneToOne
    @JoinColumn(name = "file_id")
    private CommonFile commonFile;

}
