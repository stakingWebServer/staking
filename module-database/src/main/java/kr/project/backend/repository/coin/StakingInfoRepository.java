package kr.project.backend.repository.coin;

import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StakingInfoRepository extends JpaRepository<StakingInfo, String> {
    @Query(nativeQuery = true,
            value = """
WITH ranked_staking_info AS (
    SELECT
        staking_id,
        coin_market_type,
        coin_name,
        coin_image_url,
        created_date,
        max_annual_reward_rate,
        min_annual_reward_rate,
        minimum_order_quantity,
        modified_date,
        prev_closing_price,
        reward_cycle,
        staking_status,
        unit,
        verification_fee,
        CAST(SUBSTRING(max_annual_reward_rate, LOCATE(' ', max_annual_reward_rate) + 1) AS DECIMAL(5, 2)) AS maxAnnualRewardRateNumeric,
        ROW_NUMBER() OVER (PARTITION BY coin_name ORDER BY CAST(SUBSTRING(max_annual_reward_rate, LOCATE(' ', max_annual_reward_rate) + 1) AS DECIMAL(5, 2)) DESC) AS row_num
    FROM
        staking_info
    WHERE
        created_date BETWEEN :startDate AND :endDate
)
SELECT
    staking_id,
    coin_market_type,
    coin_name,
    coin_image_url,
    created_date,
    TRIM(REGEXP_REPLACE(max_annual_reward_rate, '[가-힣]', '')) as max_annual_reward_rate,
    min_annual_reward_rate,
    minimum_order_quantity,
    modified_date,
    prev_closing_price,
    reward_cycle,
    staking_status,
    unit,
    verification_fee,
    maxAnnualRewardRateNumeric
FROM
    ranked_staking_info
WHERE
    row_num = 1
ORDER BY
    CAST(SUBSTRING(max_annual_reward_rate, LOCATE(' ', max_annual_reward_rate) + 1) AS DECIMAL(5, 2)) DESC;
""")
    List<StakingInfo> getStakingInfos(@Param(value = "startDate") LocalDateTime startDate,
                           @Param(value = "endDate")LocalDateTime endDate);

    List<StakingInfo> findByCoinNameAndCreatedDateBetween(String coinName, String startDate, String endDate);

    Optional<StakingInfo> findByCoinMarketTypeAndCoinNameAndCreatedDateBetween(CoinMarketType coinMarketType, String coinName, String createdDate, String createdDate2);

}
