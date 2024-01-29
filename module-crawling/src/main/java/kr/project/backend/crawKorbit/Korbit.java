package kr.project.backend.crawKorbit;


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

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class Korbit {
    private final StakingInfoRepository stakingInfoRepository;
    private final FavoriteRepository favoriteRepository;

    public static String korbitApi(String market) {


        RestTemplate restTemplate = new RestTemplate();

        KorbitResponseDto response = restTemplate.getForObject("https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=" + market + "_krw", KorbitResponseDto.class);
        return Objects.requireNonNull(response).getLast();
    }


    @Scheduled(cron = "0 7 0 * * *")
    public void craw() throws FileNotFoundException, InterruptedException {
        SaveDto saveDto = new SaveDto();
        String url = "https://lightning.korbit.co.kr/service/staking/list";

        //크롬드라이브 세팅
        System.setProperty("webdriver.chrome.driver", String.valueOf(ResourceUtils.getFile("/usr/bin/chromedriver")));
        //System.setProperty("webdriver.chrome.driver", String.valueOf(ResourceUtils.getFile("classpath:static/chromedriver")));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless","no-sandbox","disable-dev-shm-usage");

        //웹 주소 접속하여 페이지 열기
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get(url);
        //페이지 여는데 1초 텀 두기.
        Thread.sleep(3000);

        List<WebElement> clicks = webDriver.findElements(By.className("gaBYEM"));
        for (int i = 0; i < clicks.size(); i++) {
            List<WebElement> h3Element = webDriver.findElements(By.cssSelector(".sc-1z0oddf-0.ffIHDz h3"));
            String market = h3Element.get(i).getText().substring(h3Element.get(i).getText().indexOf("(") + 1, h3Element.get(i).getText().indexOf(")")).trim();
            List<WebElement> clickList = webDriver.findElements(By.className("gaBYEM"));
            clickList.get(i).click();
            Thread.sleep(4000);
            saveDto.setPrevClosingPrice(korbitApi(market.toLowerCase(Locale.ROOT)));
            Thread.sleep(5000);
            List<WebElement> elements = webDriver.findElements(By.cssSelector("div.sc-1ro7n4j-0 span"));
            List<Favorite> favorites = favoriteRepository.findAllByDelYn(false);
            for (int j = 0; j < elements.size(); j++) {
                saveDto.setCoinName(removeNonKorean(elements.get(0).getText()));
                saveDto.setMaxAnnualRewardRate(extractNumber(elements.get(3).getText()) + "%");
                saveDto.setMinimumOrderQuantity(elements.get(5).getText());
                saveDto.setUnit(elements.get(5).getText().trim().replaceAll("[^a-zA-Z]", ""));
                saveDto.setStakingStatus(elements.get(7).getText());
                saveDto.setCoinMarketType(CoinMarketType.korbit);
            }
            System.out.println("saveDto = " + saveDto);
            String stakingId = stakingInfoRepository.save(new StakingInfo(saveDto)).getStakingId();

            Thread.sleep(4000);

            webDriver.get(url);

            StakingInfo stakingInfo = stakingInfoRepository.findById(stakingId).orElse(null);
            favorites.forEach(favorite -> {
                if(favorite.getStakingInfo().getCoinName().equals(stakingInfo.getCoinName()) &&
                   favorite.getStakingInfo().getCoinMarketType().equals(stakingInfo.getCoinMarketType())
                ){
                    favorite.updateStakingId(stakingInfo);
                    favoriteRepository.save(favorite);
                }
            });

            Thread.sleep(3000);

        }
        //웹브라우저 닫기
        webDriver.close();
    }
    private static String extractNumber(String input) {
        // 숫자와 소수점을 포함하는 정규표현식
        String regex = "[0-9.]+";

        // 정규표현식에 매칭되는 부분 추출
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // 매칭된 부분이 있다면 반환
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null; // 매칭된 부분이 없으면 null 반환 또는 원하는 방식으로 처리
        }
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
