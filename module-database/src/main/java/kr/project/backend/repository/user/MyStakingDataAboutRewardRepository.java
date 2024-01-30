package kr.project.backend.repository.user;

import kr.project.backend.dto.user.request.RewadScheduledDto;
import kr.project.backend.entity.user.Favorite;
import kr.project.backend.entity.user.MyStakingDataAboutReward;
import kr.project.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface MyStakingDataAboutRewardRepository extends JpaRepository<MyStakingDataAboutReward,String> {

    List<MyStakingDataAboutReward> findAllByFavoriteAndUserAndUserRegDateAfter(Favorite favoriteInfo, User userInfo, String format);

    @Query(value = "SELECT new kr.project.backend.dto.user.request.RewadScheduledDto(" +
            "a.user.userId,a.userRegDate,a.favorite.favoriteId,a.myStakingDataAboutRewardId, " +
            "b.stakingInfo.stakingId,b.totalHoldings,b.totalRewards, " +
            "c.coinMarketType,c.coinName, " +
            "CAST(ROW_NUMBER() OVER (PARTITION BY a.user.userId, c.coinMarketType, c.coinName ORDER BY a.userRegDate DESC) AS int) ) " +
            "FROM MyStakingDataAboutReward a " +
            "LEFT JOIN Favorite b ON b.favoriteId = a.favorite.favoriteId AND b.delYn = false " +
            "LEFT JOIN StakingInfo c ON b.stakingInfo.stakingId = c.stakingId ")
    List<RewadScheduledDto> getLatestStakingData();

    List<MyStakingDataAboutReward> findByCompensationYn(String compensation);
}
