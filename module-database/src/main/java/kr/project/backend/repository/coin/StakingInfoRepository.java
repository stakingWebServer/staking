package kr.project.backend.repository.coin;

import kr.project.backend.entity.coin.StakingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StakingInfoRepository extends JpaRepository<StakingInfo, UUID> {

    List<StakingInfo> findAllByCreatedDateBetween(String start, String end);//test

    List<StakingInfo> findByCoinNameAndCreatedDateBetween(String coinName, String start, String end);
}
