package kr.project.backend.entity.user;

import jakarta.persistence.*;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MyStakingData implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(38)")
    @Comment(value = "마이데이터스테이킹값")
    private String myStakingDataId;
    @Comment(value = "코인명")
    private String coinName;
    @Comment(value = "코인거래소명")
    private CoinMarketType coinMarketType;
    @Comment(value = "이율")
    private String maxAnnualRewardRate;
    @Comment(value = "총 보유수량")
    private String totalHoldingsQuantity;
    @Comment(value = "총 보상수량")
    private String totalCompensationQuantity;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "myStakingData")
    private List<MyStakingDataAboutReward> myStakingDataList;
}
