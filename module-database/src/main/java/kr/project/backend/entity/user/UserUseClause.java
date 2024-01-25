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

import java.io.Serializable;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUseClause extends BaseTimeEntity implements Serializable {

    @Id
    @Comment(value = "이용약관ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long useClauseId;

    @Comment(value = "동의여부")
    @Column(columnDefinition = "char default 'N'")
    @Convert(converter = BooleanToYNConverter.class)
    private boolean agreeYn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserUseClause(User user, UseClauseDto useClauseDto){
        this.useClauseId = useClauseDto.getUseClauseId();
        this.agreeYn = Boolean.parseBoolean(useClauseDto.getUseClauseAgreeYN());
        this.user = user;
    }
}
