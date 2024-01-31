package kr.project.backend.entity.user;


import jakarta.persistence.*;
import kr.project.backend.dto.user.request.CalcStakingRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MyStakingDataAboutReward {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "myStakingDataAboutReward")
    private String myStakingDataAboutRewardId;
    @Comment(value = "일자")
    private String userRegDate;
    @Comment(value = "보상수량")
    @Column(nullable = false, precision = 27, scale = 15)
    private BigDecimal todayCompensationQuantity;

    @ManyToOne
    @JoinColumn(name = "favorite_id")
    private Favorite favorite;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}