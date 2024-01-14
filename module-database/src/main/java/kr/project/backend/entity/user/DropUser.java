package kr.project.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.project.backend.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DropUser extends BaseTimeEntity implements Serializable {

    /**
     * 탈퇴키값
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @NotNull
    @Comment(value = "탈퇴유저 키값")
    private UUID dropId;

    @Comment(value = "유저 이메일")
    private String userEmail;

    @Comment(value = "탈퇴일시")
    private String dropDttm;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public DropUser(User userInfo) {
        this.userEmail = userInfo.getUserEmail();
        this.dropDttm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.user = userInfo;
    }
}
