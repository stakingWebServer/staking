package kr.project.backend.controller.admin;


import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.project.backend.dto.common.request.PushRequestDto;
import kr.project.backend.dto.common.request.PushsRequestDto;
import kr.project.backend.results.ListResult;
import kr.project.backend.service.admin.AdminService;
import kr.project.backend.common.Environment;
import kr.project.backend.results.ObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자", description = "관리자 로그인")
@Slf4j
@RestController
@RequestMapping("/api/" + Environment.API_VERSION + "/" + Environment.API_ADMIN)
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "apiKey 발급",description = "관리자용 헤더 apikey를 발급합니다.")
    @GetMapping("/accessKey/{plainText}")
    public ResponseEntity<?> accessKey(@Parameter(name = "plainText", description = "암호화 할 평문", example = "testText")
                                       @PathVariable(name = "plainText") String plainText) throws Exception{
        return ObjectResult.build(adminService.giveApikey(plainText));
    }
    @Operation(summary = "당일 가입 사용자 수",description = "당일 가입 사용자 수를 구한다.")
    @GetMapping("/today-register")
    public ResponseEntity<?> getTodayRegister(){
        return ObjectResult.build(adminService.getTodayRegister());
    }

    @Operation(summary = "당일 로그인 사용자 수",description = "당일 로그인 사용자 수를 구한다.")
    @GetMapping("/today-loginUser")
    public ResponseEntity<?> getTodayLoginUser(){
        return ObjectResult.build(adminService.getTodayLoginUser());
    }
    @Operation(summary = "당일 탈퇴 사용자 수",description = "당일 탈토ㅚ 사용자 수를 구한다.")
    @GetMapping("/today-dropUser")
    public ResponseEntity<?> getTodayDropUser(){
        return ObjectResult.build(adminService.getTodayDropUser());
    }

    @Operation(summary = "페이지 별 조회 수",description = "패아자 별 조회 수를 구한다.")
    @GetMapping("/page-view")
    public ResponseEntity<?> getPageView(){
        return ListResult.build(adminService.getPageView());
    }
    @Operation(summary = "단일 푸시 토큰 발송",description = "단일 푸시 토큰 발송을 한다.")
    @PostMapping("/send-push")
    public ResponseEntity<?> push(@RequestBody PushRequestDto pushRequestDto) throws FirebaseMessagingException{
        adminService.sendPush(pushRequestDto);
        return ObjectResult.ok();
    }
    @Operation(summary = "단체 푸시 토큰 발송",description = "단체 푸시 토큰 발송을 한다.")
    @PostMapping("/send-pushs")
    public ResponseEntity<?> pushs(@RequestBody PushsRequestDto pushsRequestDto) throws FirebaseMessagingException {
        adminService.sendPushs(pushsRequestDto);
        return ObjectResult.ok();
    }
    @Operation(summary = "문의 목록",description = "문의 목록을 조회합니다.")
    @PostMapping("/questions")
    public ResponseEntity<?> questions(){
        return ListResult.build(adminService.getQuestions());
    }
}