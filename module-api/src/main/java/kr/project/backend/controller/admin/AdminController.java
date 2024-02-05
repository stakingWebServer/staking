package kr.project.backend.controller.admin;


import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.project.backend.dto.admin.request.AdminLoginRequestDto;
import kr.project.backend.dto.admin.request.ReplyRequestDto;
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


    @Operation(summary = "관리자 로그인", description = "로그인 성공하면, api-key를 준다.")
    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody AdminLoginRequestDto adminLoginRequestDto) throws Exception {
        return ObjectResult.build(adminService.getApikey(adminLoginRequestDto));
    }

    @Operation(summary = "당일 가입 사용자 수 | 당일 로그인 사용자 수 | 당일 탈퇴 사용자 수", description = "당일 가입 사용자 수 | 당일 로그인 사용자 수 | 당일 탈퇴 사용자 수 구한다")
    @GetMapping("/today/{todayStatus}")
    public ResponseEntity<?> getTodayInfos(@Parameter(description = "register | loginUser | dropUser", example = "register", required = true)
                                           @PathVariable(value = "todayStatus") String todayStatus) {
        return ObjectResult.build(adminService.getTodayInfos(todayStatus));
    }

    @Operation(summary = "페이지 별 조회 수", description = "패아자 별 조회 수를 구한다.")
    @GetMapping("/page-view")
    public ResponseEntity<?> getPageView() {
        return ListResult.build(adminService.getPageView());
    }

    @Operation(summary = "단일 푸시 토큰 발송", description = "단일 푸시 토큰 발송을 한다.")
    @PostMapping("/push")
    public ResponseEntity<?> push(@RequestBody PushRequestDto pushRequestDto) throws FirebaseMessagingException {
        adminService.sendPush(pushRequestDto);
        return ObjectResult.ok();
    }

    @Operation(summary = "단체 푸시 토큰 발송", description = "단체 푸시 토큰 발송을 한다.")
    @PostMapping("/pushs")
    public ResponseEntity<?> pushs(@RequestBody PushsRequestDto pushsRequestDto) throws FirebaseMessagingException {
        adminService.sendPushs(pushsRequestDto);
        return ObjectResult.ok();
    }

    @Operation(summary = "문의 목록", description = "문의 목록을 조회합니다.")
    @GetMapping("/questions")
    public ResponseEntity<?> questions() {
        return ListResult.build(adminService.getQuestions());
    }

    @Operation(summary = "문의에 대한 답변", description = "문의에 대한 답변을 합니다.")
    @PostMapping("/reply")
    public ResponseEntity<?> post(@RequestBody ReplyRequestDto replyRequestDto) throws FirebaseMessagingException {
        adminService.replyAboutQuestion(replyRequestDto);
        return ObjectResult.ok();
    }
}