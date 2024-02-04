package kr.project.backend.repository.coin;

import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StakingInfoRepository extends JpaRepository<StakingInfo, String> {

    List<StakingInfo> findAllByCreatedDateBetween(String startDate, String endDate);

    List<StakingInfo> findAllByCreatedDateBetweenOrderByMaxAnnualRewardRateDesc(String coinName, String startDate, String endDate);

    Optional<StakingInfo> findByCoinMarketTypeAndCoinNameAndCreatedDateBetween(CoinMarketType coinMarketType, String coinName, String createdDate, String createdDate2);

    List<StakingInfo> findAllByCreatedDateBetweenOrderByMaxAnnualRewardRateDesc(String format, String format1);
}
