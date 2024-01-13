package kr.project.database.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.project.database.entity.coin.StakingInfo;
import kr.project.database.common.BaseTimeEntity;
import kr.project.database.converter.BooleanToYNConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Getter
@DynamicInsert
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @NotNull
    @Comment(value = "즐겨찾기 키값")
    private UUID favoriteId;
    @Comment(value = "총 보유수량")
    private double totalHoldings;
    @Comment(value = "총 보상수량")
    private double totalRewards;

    @Column(columnDefinition = "char default 'N'")
    @Convert(converter = BooleanToYNConverter.class)
    @Comment(value = "삭제 유무")
    private boolean delYn;

    @ManyToOne
    @JoinColumn(name = "staking_id")
    private StakingInfo stakingInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Favorite(User userInfo, StakingInfo stakingInfo) {
        this.stakingInfo = stakingInfo;
        this.user = userInfo;
    }

    public void unFavorite() {
        this.delYn = true;
    }
}
