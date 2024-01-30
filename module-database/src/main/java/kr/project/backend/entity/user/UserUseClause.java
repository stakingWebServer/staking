package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.entity.common.BaseTimeEntity;
import kr.project.backend.converter.BooleanToYNConverter;
import kr.project.backend.dto.user.request.UseClauseDto;
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
public class UserUseClause extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "이용약관ID")
    private String userUseClauseId;

    @Comment(value = "동의여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String agreeYn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "use_clause_id")
    private UseClause useClause;

    public UserUseClause(User user, UseClause useClause, UseClauseDto useClauseDto){
        this.agreeYn = useClauseDto.getUseClauseAgreeYN();
        this.user = user;
        this.useClause = useClause;
    }
}
