package kr.project.backend.entity.user;


import jakarta.persistence.*;
import kr.project.backend.dto.user.request.CalcStakingRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

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
    private String todayCompensationQuantity;

    @ManyToOne
    @JoinColumn(name = "myStakingData_id")
    private MyStakingData myStakingData;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public MyStakingDataAboutReward(CalcStakingRequestDto calcStakingRequestDto, User userInfo, MyStakingData myStakingData) {
        this.userRegDate = calcStakingRequestDto.getUserRegDate();
        this.todayCompensationQuantity = String.valueOf(Double.parseDouble(myStakingData.getMaxAnnualRewardRate()) * Double.parseDouble(calcStakingRequestDto.getInsertAmount()));
        this.user = userInfo;
        this.myStakingData = myStakingData;
    }
}
