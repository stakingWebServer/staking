package kr.project.backend.controller.admin;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.project.backend.service.admin.AdminService;
import kr.project.backend.common.Environment;
import kr.project.backend.results.ObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    //TODO 대시보드
    // - 당일 가입 사용자 수 : user -> regDate로 판단
    // - 당일 로그인 사용자 수 : 칼럼을 추가하여 login할때의 날짜를 확인할수있도록 추가
    // - 당일 탈퇴 사용자 수 : DropUser 테이블에서 regDate로 판단
    // - 페이지별 조회 수 : ???
    @Operation(summary = "당일 가입 사용자 수",description = "당일 가입 사용자 수를 구한다.")
    @GetMapping("/todayRegister")
    public ResponseEntity<?> getTodayRegister(){
        return ObjectResult.build(adminService.getTodayRegister());
    }








}