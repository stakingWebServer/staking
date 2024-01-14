package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity implements Serializable {

    /**
     * 리프레시토큰 키값
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @NotNull
    @Comment(value = "리프레시토큰 키값")
    private UUID refreshTokenId;

    @Comment(value = "리프레시토큰값")
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public RefreshToken(String refreshToken, User userInfo) {
        this.refreshToken = refreshToken;
        this.user = userInfo;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
