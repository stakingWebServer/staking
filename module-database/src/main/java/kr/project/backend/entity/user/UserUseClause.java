package kr.project.database.entity.user;

import jakarta.persistence.*;
import kr.project.database.common.BaseTimeEntity;
import kr.project.database.converter.BooleanToYNConverter;
import kr.project.database.dto.user.request.UseClauseDto;
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
    private UUID useClauseId;

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
