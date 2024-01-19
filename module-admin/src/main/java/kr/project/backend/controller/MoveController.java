package kr.project.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoveController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/index")
    public String index2(){
        return "index";
    }
    @GetMapping("/tables")
    public String tables(){
        return "tables";
    }
    @GetMapping("/qna")
    public String qna(){
        return "qna";
    }
    @GetMapping("/push")
    public String push(){
        return "push";
    }
    @GetMapping("/charts")
    public String chart(){
        return "charts";
    }
}
