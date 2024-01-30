package kr.project.backend.schedule;

import kr.project.backend.dto.user.request.RewadScheduledDto;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.user.Favorite;
import kr.project.backend.entity.user.MyStakingDataAboutReward;
import kr.project.backend.entity.user.User;
import kr.project.backend.repository.coin.StakingInfoRepository;
import kr.project.backend.repository.user.FavoriteRepository;
import kr.project.backend.repository.user.MyStakingDataAboutRewardRepository;
import kr.project.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class schedule {

    private final UserRepository userRepository;
    private final StakingInfoRepository stakingInfoRepository;
    private final FavoriteRepository favoriteRepository;
    private final MyStakingDataAboutRewardRepository myStakingDataAboutRewardRepository;

    //@Scheduled(cron = "0 * * * * *")
    public void rewardHistory() {
        /* ***************************************************************************************
         * - 보유수량, 보상수량 update
         * 1. 유저,코인마켓,코인이름 group by 후 최상단 데이터 추출
         * 2. user_reg_date 필드를통해 24시간이 지났는지 체크
         * 3. 24시간이 지난 항목은 이율 계산후 보상내역에 새로 삽입
         * **************************************************************************************/



        /* ***************************************************************************************
         * - 보상내역 테이블 insert
         * 1. 유저,코인마켓,코인이름 group by 후 최상단 데이터 추출
         * 2. user_reg_date 필드를통해 24시간이 지났는지 체크
         * 3. 24시간이 지난 항목은 이율 계산후 보상내역에 새로 삽입
         * **************************************************************************************/
        List<RewadScheduledDto> rewadScheduledDtoList = myStakingDataAboutRewardRepository.getLatestStakingData();
        //TODO 쿼리에서 cnt 1만 뽑아오는걸로 수정해야함
        List<RewadScheduledDto> filteredList = rewadScheduledDtoList.stream()
                .filter(dto -> dto.getCnt() != null && dto.getCnt() == 1)
                .collect(Collectors.toList());

        //보상내역 추가로직
        for(RewadScheduledDto rewadScheduledDto : filteredList){
            //즐겨찾기 조회
            Favorite favorite = favoriteRepository.findById(rewadScheduledDto.getFavoriteId()).orElse(null);
            //스테이킹 조회
            StakingInfo stakingInfo = stakingInfoRepository.findById(favorite.getStakingInfo().getStakingId()).orElse(null);
            //유저 조회
            User user = userRepository.findById(favorite.getUser().getUserId()).orElse(null);
            //탈퇴했을경우 체크
            if (user == null){
                return;
            }
            //최초 수량입력한 시간에 24시간이 지났으면 보상 내역에 추가
            LocalDateTime inputDateTime = LocalDateTime.parse(rewadScheduledDto.getUserRegDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime currentDateTime = LocalDateTime.now();
            long hoursBetween = ChronoUnit.HOURS.between(inputDateTime, currentDateTime);
            if(24 < Integer.parseInt(String.valueOf(hoursBetween))){
                //보상률에서 숫자랑 소수점만 추출
                String regex = "[0-9.]+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(stakingInfo.getMaxAnnualRewardRate());
                String maxAnnualRewardRate = "";
                while (matcher.find()) {
                    maxAnnualRewardRate = matcher.group();
                }
                //보상률 계산
                BigDecimal todayCompensationQuantity = favorite.getTotalHoldings().multiply(new BigDecimal(String.valueOf(maxAnnualRewardRate))).divide(new BigDecimal("365"), RoundingMode.HALF_UP);
                //보상내역에 추가 저장
                myStakingDataAboutRewardRepository.save(new MyStakingDataAboutReward(favorite,user,todayCompensationQuantity,favorite.getTotalHoldings()));
            }
        }

    }

    public String changeRewardCycle(String rewardCycle){
        String r = "";

        //ex.매일
        if(rewardCycle.equals("매일")){
            r = "24";
        }

        //ex.6일 이내 / 2~3일, 6일 이내 / 11일
        if(rewardCycle.contains("/")){
            rewardCycle = rewardCycle.substring(rewardCycle.indexOf("/")+1).trim();
            if(rewardCycle.contains("~")){
                rewardCycle = rewardCycle.substring(rewardCycle.indexOf("~")+1).trim();
            }
            // 숫자만을 추출하는 정규표현식
            String regex = "\\d+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(rewardCycle);
            while (matcher.find()) {
                rewardCycle = matcher.group();
            }
            int result = Integer.parseInt(rewardCycle) * 24;
            r = String.valueOf(result);
        }

        return r;
    }


}
