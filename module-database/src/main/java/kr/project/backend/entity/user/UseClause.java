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
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UseClause extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "이용약관 키값")
    private Long useClauseId;

    @Comment(value = "이용약관 제목")
    @Column(length = 50)
    private String useClauseTitle;

    @Comment(value = "이용약관 필수여부")
    @Column(columnDefinition = "char default 'N'")
    @Convert(converter = BooleanToYNConverter.class)
    private boolean useClauseEssentialYn;

    @Comment(value = "이용약관 구분")
    @Column(length = 2)
    private String useClauseKind; //

    @Comment(value = "이용약관 상태")
    @Column(length = 2)
    private String useClauseState;

    @OneToOne
    @JoinColumn(name = "file_id")
    private CommonFile commonFile;

}
