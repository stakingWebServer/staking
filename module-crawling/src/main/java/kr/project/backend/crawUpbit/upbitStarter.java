package kr.project.backend.crawUpbit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class upbitStarter {

    private final Upbit upbit;
    @PostMapping("/craw-upbit")
    public void post() throws IOException, InterruptedException {
        upbit.craw();
    }
}
