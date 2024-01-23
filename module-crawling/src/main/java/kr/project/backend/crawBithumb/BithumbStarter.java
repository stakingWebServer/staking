package kr.project.backend.crawBithumb;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
public class BithumbStarter {
    private final Bithumb bithumb;

  /*  @PostMapping("/craw-bithumb")
    public void post() throws FileNotFoundException, InterruptedException {
        bithumb.craw();
    }*/
}
