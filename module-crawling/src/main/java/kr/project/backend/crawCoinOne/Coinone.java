package kr.project.backend.crawCoinOne;

import kr.project.backend.dto.coin.SaveDto;
import kr.project.backend.entity.coin.StakingInfo;
import kr.project.backend.entity.coin.enumType.CoinMarketType;
import kr.project.backend.repository.coin.StakingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class Coinone {

    private final StakingInfoRepository stakingInfoRepository;

    @Scheduled(cron = "0 30 1 * * *")
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


        //각 요소 추출
        List<WebElement> coinNames = webDriver.findElements(By.className("ProductsBrowseList_coin-name__bSWTj"));
        List<WebElement> years = webDriver.findElements(By.className("ProductsBrowseList_column-reward__mKBuy"));

        for (int i = 0; i < coinNames.size(); i++) {
            saveDto.setCoinName(coinNames.get(i).getText());
            saveDto.setMaxAnnualRewardRate(years.get(i).getText());
            saveDto.setCoinMarketType(CoinMarketType.coinone);
            stakingInfoRepository.save(new StakingInfo(saveDto));
            System.out.println("saveDto = " + saveDto);
        }
        Thread.sleep(4000);
        //웹브라우저 닫기
        webDriver.close();



    }
}
