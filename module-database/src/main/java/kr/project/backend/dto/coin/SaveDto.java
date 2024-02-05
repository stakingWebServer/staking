package kr.project.backend.dto.coin;


import kr.project.backend.entity.coin.enumType.CoinMarketType;
import lombok.Data;

@Data
public class SaveDto {
    private String coinName; //코인이름
    private String coinImageUrl; //코인이미지주소
    private String unit; //단위
    private String prevClosingPrice; //전일 종가
    private String minAnnualRewardRate; //연 추정 보상률 (최소)
    private String maxAnnualRewardRate; //연 추정 보상률 (최대)
    private String stakingStatus; //스테이킹/언스테이킹 대기
    private String rewardCycle; //보상주기
    private String minimumOrderQuantity; // 최소신청수량
    private String verificationFee; //검증인 수수료
    private CoinMarketType coinMarketType; //코인거래소 종류

}
