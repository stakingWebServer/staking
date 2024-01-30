package kr.project.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.common.BaseTimeEntity;
import kr.project.backend.converter.BooleanToYNConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@DynamicInsert
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "즐겨찾기 키값")
    private String favoriteId;
    @Comment(value = "총 보유수량")
    @Column(nullable = false, precision = 27, scale = 15)
    private BigDecimal totalHoldings; 
    @Comment(value = "총 보상수량")
    @Column(nullable = false, precision = 27, scale = 15)
    private BigDecimal totalRewards;

    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    @Convert(converter = BooleanToYNConverter.class)
    @Comment(value = "삭제 유무")
    private boolean delYn;

    @ManyToOne
    @JoinColumn(name = "staking_id")
    private StakingInfo stakingInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "favorite")
    private List<MyStakingDataAboutReward> myStakingDataAboutRewardList;


    public Favorite(User userInfo, StakingInfo stakingInfo) {
        this.stakingInfo = stakingInfo;
        this.user = userInfo;
    }

    public void unFavorite() {
        this.delYn = true;
    }

    public void updateStakingId(StakingInfo stakingInfo) {
        this.stakingInfo = stakingInfo;
    }
}
