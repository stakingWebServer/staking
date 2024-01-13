package com.craw.crawlingprogram.crawKorbit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
public class CrawlingStarter4 {

    private final Korbit korbit;

    @PostMapping("/craw-korbit")
    public void post() throws FileNotFoundException, InterruptedException {
        korbit.craw();
    }

}
