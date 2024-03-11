package kr.project.backend.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/oauth/login/kakao-page")
    public String kakaoView(){
        return "kakaoPage";
    }
}
