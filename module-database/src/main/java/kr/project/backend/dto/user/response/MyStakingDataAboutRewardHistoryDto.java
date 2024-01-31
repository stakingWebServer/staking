package kr.project.backend.dto.user.response;

import kr.project.backend.entity.user.MyStakingDataAboutReward;
import lombok.Data;

import java.text.DecimalFormat;

@Data
public class MyStakingDataAboutRewardHistoryDto {
    private String userRegDate;
    private String todayCompensationQuantity;

    public MyStakingDataAboutRewardHistoryDto(MyStakingDataAboutReward myStakingDataAboutReward){
        DecimalFormat decimalFormat = new DecimalFormat("#.##################");
        this.userRegDate = myStakingDataAboutReward.getUserRegDate().substring(0,10);
        this.todayCompensationQuantity = decimalFormat.format(myStakingDataAboutReward.getTodayCompensationQuantity());
    }
}
