package kr.project.backend.crawBithumb;

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
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class Bithumb {

    private final StakingInfoRepository stakingInfoRepository;


    public static String BithumbApi(String market) {

        //upbit api 요청으로 전일종가 추출
        RestTemplate restTemplate = new RestTemplate();

        BithumbResponseDto response = restTemplate.getForObject("https://api.bithumb.com/public/ticker/"+market + "_KRW", BithumbResponseDto.class);
        System.out.println("response = " + response);
        return response.getData().getClosing_price();
    }//tests
    //@Scheduled(cron = "0 0 2 * * *")
    public void craw() throws FileNotFoundException, InterruptedException {
        SaveDto saveDto = new SaveDto();
        String url = "https://www.bithumb.com/staking/goods";

        //크롬드라이브 세팅
        //System.setProperty("webdriver.chrome.driver", String.valueOf(ResourceUtils.getFile("/usr/bin/chromedriver")));
        System.setProperty("webdriver.chrome.driver", String.valueOf(ResourceUtils.getFile("classpath:static/chromedriver")));
        ChromeOptions options = new ChromeOptions();
        //배포할때 주석풀기.
        options.addArguments("headless","no-sandbox","disable-dev-shm-usage");
        //웹 주소 접속하여 페이지 열기
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get(url);
        //페이지 여는데 1초 텀 두기.
        Thread.sleep(3000);


        //더보기 버튼 클릭
        /*WebElement buttonClick = webDriver.findElement(By.id("moreAvailBtn"));
        buttonClick.click();*/

        //각 요소 추출
        List<WebElement> coinName = webDriver.findElements(By.className("staking-good-lego-coin__text"));
        List<WebElement> years = webDriver.findElements(By.className("staking-good-lego-apy__number"));
        List<WebElement> numbers = webDriver.findElements(By.className("staking-good-lego-item__number"));
        List<WebElement> unit = webDriver.findElements(By.className("staking-good-lego-item__name"));


        for (int i = 0; i < coinName.size(); i++) {
            System.out.println("value = " + unit.get(i).getText());
            saveDto.setPrevClosingPrice(BithumbApi(unit.get(i).getText()));
            Thread.sleep(5000);
            saveDto.setCoinName(coinName.get(i).getText());
            String[] values = extractNumbers(years.get(i).getText());
            saveDto.setMinAnnualRewardRate(values[0]);
            saveDto.setMaxAnnualRewardRate(values[1]);
            saveDto.setMinimumOrderQuantity(numbers.get(i).getText() + unit.get(i).getText());
            saveDto.setCoinMarketType(CoinMarketType.bithumb);
            //stakingInfoRepository.save(new StakingInfo(saveDto));
            System.out.println("saveDto = " + saveDto);
        }

        Thread.sleep(3000);
        //웹브라우저 닫기
        webDriver.close();
    }
    private static String[] extractNumbers(String input) {
        // 숫자와 소수점을 포함하는 정규표현식
        String regex = "[0-9.]+%";

        // 정규표현식에 매칭되는 부분 추출
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // 매칭된 부분 저장
        String[] extractedNumbers = new String[2];
        int index = 0;
        while (matcher.find() && index < 2) {
            extractedNumbers[index++] = matcher.group();
        }

        return extractedNumbers;
    }
    private static String extractLetterAfterNumber(String input) {
        // 정규표현식 패턴
        String pattern = "(\\d*\\.?\\d+)([A-Za-z]+)";

        // 정규표현식과 매칭
        if (input.matches(pattern)) {
            // 매칭된 부분 반환 (영어 부분)
            System.out.println("input.replaceAll(pattern, \"$2\") = " + input.replaceAll(pattern, "$2"));
            return input.replaceAll(pattern, "$2");
        } else {
            return "매칭된 결과 없음";
        }
    }
}


