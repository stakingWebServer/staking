package kr.project.backend.crawKorbit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
public class KorbitStarter {

    private final Korbit korbit;

    @PostMapping("/craw-korbit")
    public void post() throws FileNotFoundException, InterruptedException {
        korbit.craw();
    }

}
