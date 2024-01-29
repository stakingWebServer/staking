package kr.project.backend.crawCoinOne;

import kr.project.backend.dto.coin.SaveDto;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.repository.coin.StakingInfoRepository;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class Coinone {

    private final StakingInfoRepository stakingInfoRepository;

    @Scheduled(cron = "0 14 0 * * *")
    public void craw() throws FileNotFoundException, InterruptedException {
        SaveDto saveDto = new SaveDto();
        String url = "https://coinone.co.kr/plus";

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

        List<WebElement> elements = webDriver.findElements(By.cssSelector("[class^='glyph-coin-']"));

        List<String> unit = new ArrayList<>();
        // 정규 표현식을 사용하여 클래스 이름에서 값을 추출
        Pattern pattern = Pattern.compile("glyph-coin-(.*)");
        for (WebElement element : elements) {
            String className = element.getAttribute("class");
            Matcher matcher = pattern.matcher(className);

            // 매칭된 부분이 있는 경우 값을 출력
            if (matcher.find()) {
                unit.add(matcher.group(1).toUpperCase());
            }
        }

        //각 요소 추출
        List<WebElement> coinNames = webDriver.findElements(By.className("ProductsBrowseList_coin-name__bSWTj"));
        List<WebElement> years = webDriver.findElements(By.className("ProductsBrowseList_column-reward__mKBuy"));

        for (int i = 0; i < coinNames.size(); i++) {
            saveDto.setCoinName(coinNames.get(i).getText());
            saveDto.setMaxAnnualRewardRate(years.get(i+1).getText());
            saveDto.setCoinMarketType(CoinMarketType.coinone);
            saveDto.setUnit(unit.get(i));
            stakingInfoRepository.save(new StakingInfo(saveDto));
            System.out.println("saveDto = " + saveDto);
        }
        Thread.sleep(4000);
        //웹브라우저 닫기
        webDriver.close();



    }
}
