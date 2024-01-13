package com.craw.crawlingprogram.crawCoinOne;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
public class CrawlingStarter3 {
    private final Coinone coinone;

    @PostMapping("/craw-coinone")
    public void post() throws FileNotFoundException, InterruptedException {
        coinone.craw();
    }

}
