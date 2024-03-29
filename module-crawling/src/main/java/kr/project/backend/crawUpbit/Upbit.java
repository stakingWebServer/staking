package kr.project.backend.crawUpbit;

import kr.project.backend.dto.coin.SaveDto;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.entity.user.Favorite;
import kr.project.backend.repository.coin.StakingInfoRepository;
import kr.project.backend.repository.user.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Slf4j
@Service
public class Upbit {
    private final StakingInfoRepository stakingInfoRepository;
    private final FavoriteRepository favoriteRepository;

    public static String upbitApi(String market) {

        //upbit api 요청으로 전일종가 추출

            RestTemplate restTemplate = new RestTemplate();

            UpbitResponseDto[] response = restTemplate.getForObject("https://api.upbit.com/v1/ticker?markets=KRW-" + market, UpbitResponseDto[].class);
            return String.valueOf(response[0].getPrev_closing_price());
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void craw() throws IOException, InterruptedException {
        log.info("::: upbit crawling start :::");
        SaveDto saveDto = new SaveDto();

        //크롤링할 주소
        String url = "https://upbit.com/staking/items";
        //크롬드라이브 세팅
        System.setProperty("webdriver.chrome.driver", String.valueOf(ResourceUtils.getFile("/usr/bin/chromedriver")));
        //System.setProperty("webdriver.chrome.driver", String.valueOf(ResourceUtils.getFile("classpath:static/chromedriver")));
        ChromeOptions options = new ChromeOptions();
        //배포할때 주석풀기.
        options.addArguments("headless","no-sandbox","disable-dev-shm-usage");
        //웹 주소 접속하여 페이지 열기
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get(url);
        //페이지 여는데 1초 텀 두기.
        Thread.sleep(3000);

        List<WebElement> addTexts = webDriver.findElements(By.className("css-1j71w0l"));
        List<Favorite> favorites = favoriteRepository.findAllByDelYn(false);
        for (int i =0; i<addTexts.size(); i++) {
            List<WebElement> addTextList = webDriver.findElements(By.className("css-1j71w0l"));
            addTextList.get(i).click();
            Thread.sleep(3000);
            //코인이름
            WebElement coinName = webDriver.findElement(By.className("ListDetailView__condition__title__text"));
            String market = coinName.getText().substring(coinName.getText().indexOf("(") + 1, coinName.getText().indexOf(")")).trim();
            saveDto.setCoinName(removeNonKorean(coinName.getText()));
            //코인이미지url
            WebElement coinImageUrl = webDriver.findElement(By.cssSelector("span.ListDetailView__condition__title__logo img"));
            String srcValue = coinImageUrl.getAttribute("src");
            saveDto.setCoinImageUrl(srcValue);
            //unit
            WebElement rawUnit = webDriver.findElement(By.className("ListDetailView__condition__title__unit"));
            String unit = rawUnit.getText().replaceAll("[^a-zA-Z]", "");
            saveDto.setUnit(unit);
            //코인전날 종가 api로 받기
            saveDto.setPrevClosingPrice(upbitApi(market));
            Thread.sleep(5000);

            //연 추정 보상률, 스테이킹/언스테이킹 대기, 보상주기
            List<WebElement> values = webDriver.findElements(By.className("infoItem__value"));
            int valueIndex = 0;
            for (WebElement value : values) {
                if(valueIndex == 0){
                    saveDto.setMaxAnnualRewardRate(value.getText());
                }
                if(valueIndex == 1){
                    saveDto.setStakingStatus(value.getText());
                }
                if(valueIndex == 2){
                    saveDto.setRewardCycle(value.getText());
                }
                valueIndex++;
            }


            //최소신청수량, 검증인 수수료
            List<WebElement> values2 = webDriver.findElements(By.className("conditionInfo__data__value--Num"));
            int miniAndFeeIndex = 0;
            for (WebElement value:values2){
                if(miniAndFeeIndex == 0){
                    saveDto.setMinimumOrderQuantity(value.getText());
                }
                if(miniAndFeeIndex == 1){
                    saveDto.setVerificationFee(value.getText());
                }
                miniAndFeeIndex++;
            }
            //거래소 저장
            saveDto.setCoinMarketType(CoinMarketType.업비트);
            String stakingId = stakingInfoRepository.save(new StakingInfo(saveDto)).getStakingId();
            System.out.println("saveDto :::::" + saveDto);

            Thread.sleep(4000);

            //스테이킹 목록으로 다시들어가기
            webDriver.get(url);

            StakingInfo stakingInfo  = stakingInfoRepository.findById(stakingId).orElse(null);
            favorites.forEach(favorite -> {
                if(favorite.getStakingInfo().getCoinName().equals(stakingInfo.getCoinName()) &&
                   favorite.getStakingInfo().getCoinMarketType().equals(stakingInfo.getCoinMarketType())
                ){
                    favorite.updateStakingId(stakingInfo);
                    //업데이트
                    favoriteRepository.save(favorite);
                }
            });
            Thread.sleep(4000);
        }
        //웹브라우저 닫기
        webDriver.close();
        log.info("::: upbit crawling finish :::");
    }

    public static String removeNonKorean(String input) {
        // 정규표현식을 사용하여 한글을 제외한 모든 문자를 제거
        String regex = "[가-힣]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // 매칭된 부분을 제외한 나머지 부분을 합침
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group());
        }

        return result.toString();
    }

}

