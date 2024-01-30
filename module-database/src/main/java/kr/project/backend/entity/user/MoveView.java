package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.dto.user.request.MoveViewRequestDto;
import kr.project.backend.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MoveView extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "화면이동ID")
    private Long viewId;

    @Column(length = 100)
    @Comment(value = "화면명")
    private String viewName;

    @Column(length = 2)
    @Comment(value = "회원가입 os 구분")
    private String userJoinOsKind;  

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public MoveView(User userInfo, MoveViewRequestDto moveViewRequestDto){
        this.viewName = moveViewRequestDto.getViewName();
        this.userJoinOsKind = userInfo.getUserJoinOsKind();
        this.user = userInfo;
    }

}
