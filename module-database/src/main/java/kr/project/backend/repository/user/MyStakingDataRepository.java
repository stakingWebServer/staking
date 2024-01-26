package kr.project.backend.repository.user;

import kr.project.backend.entity.user.MyStakingData;
import kr.project.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyStakingDataRepository extends JpaRepository<MyStakingData,String> {
    List<MyStakingData> findAllByUser(User userInfo);

    Optional<MyStakingData> findByMyStakingDataIdAndUser(String myStakingDataId, User userInfo);
}
