package kr.project.backend.repository.user;

import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.user.Favorite;
import kr.project.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, String> {

    Optional<Favorite> findByStakingInfoAndUserAndDelYn(StakingInfo stakingInfo, User userInfo, boolean delYn);



    List<Favorite> findAllByUserAndDelYn(User userInfo, boolean delYn);


    boolean existsByStakingInfoAndUserAndDelYn(StakingInfo stakingInfo, User userInfo, boolean delYn);

    Optional<Favorite> findByFavoriteIdAndUserAndDelYn(String favoriteId, User userInfo, boolean b);

}
