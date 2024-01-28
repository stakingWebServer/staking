package kr.project.backend.entity.coin;

import jakarta.persistence.*;
import kr.project.backend.dto.coin.SaveDto;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.entity.common.BaseTimeEntity;
import kr.project.backend.entity.user.Favorite;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StakingInfo extends BaseTimeEntity implements Serializable {
    @Id//text
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "스테이킹키값")
    private String stakingId;
    @Comment(value = "코인이름")
    private String coinName;

    @Comment(value = "단위")
    private String unit;
    @Comment(value = "전일종가")
    private String  prevClosingPrice;
    @Comment(value = "연 추정 보상률 (최소)")
    private String minAnnualRewardRate;
    @Comment(value = "연 추정 보상률 (최대)")
    private String maxAnnualRewardRate;
    @Comment(value = "스테이킹/언스테이킹 대기")
    private String stakingStatus;
    @Comment(value = "보상주기")
    private String rewardCycle;
    @Comment(value = "최소신청수량")
    private String minimumOrderQuantity;
    @Comment(value = "검증인 수수료")
    private String verificationFee;
    @Comment(value = "코인거래소 종류")
    @Enumerated(EnumType.STRING)
    private CoinMarketType coinMarketType;

    @OneToMany(mappedBy = "stakingInfo")
    public List<Favorite> favorites;//d

    public StakingInfo(SaveDto saveDto){
        this.coinName = saveDto.getCoinName();
        this.unit = saveDto.getUnit();
        this.prevClosingPrice = saveDto.getPrevClosingPrice();
        this.minAnnualRewardRate = saveDto.getMinAnnualRewardRate();
        this.maxAnnualRewardRate = saveDto.getMaxAnnualRewardRate();
        this.stakingStatus = saveDto.getStakingStatus();
        this.rewardCycle = saveDto.getRewardCycle();
        this.minimumOrderQuantity = saveDto.getMinimumOrderQuantity();
        this.verificationFee = saveDto.getVerificationFee();
        this.coinMarketType = saveDto.getCoinMarketType();
    }
}
