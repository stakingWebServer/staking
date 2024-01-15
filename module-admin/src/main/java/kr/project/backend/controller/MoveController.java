package kr.project.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoveController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
