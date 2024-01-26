package kr.project.backend.repository.user;

import kr.project.backend.entity.user.MyStakingData;
import kr.project.backend.entity.user.MyStakingDataAboutReward;
import kr.project.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MyStakingDataAboutRewardRepository extends JpaRepository<MyStakingDataAboutReward,String> {
    List<MyStakingDataAboutReward> findAllByMyStakingDataAndUserAndUserRegDateAfter(MyStakingData myStakingData, User userInfo, String startDate);
}
