package kr.project.backend.repository.user;

import kr.project.backend.entity.user.Favorite;
import kr.project.backend.entity.user.MyStakingDataAboutReward;
import kr.project.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MyStakingDataAboutRewardRepository extends JpaRepository<MyStakingDataAboutReward,String> {

    List<MyStakingDataAboutReward> findAllByFavoriteAndUserAndUserRegDateAfter(Favorite favoriteInfo, User userInfo, String format);

    Page<MyStakingDataAboutReward> findByFavorite(Favorite favorite, Pageable pageable);
}
