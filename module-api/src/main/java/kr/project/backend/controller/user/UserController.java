package kr.project.backend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.project.backend.auth.ServiceUser;
import kr.project.backend.results.ListResult;
import kr.project.backend.service.user.UserService;
import kr.project.backend.common.Environment;
import kr.project.backend.dto.user.request.*;
import kr.project.backend.results.ObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "account", description = "로그인 / 회원가입")
@Slf4j
@RestController
@RequestMapping("/api/" + Environment.API_VERSION + "/" + Environment.API_USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "일반 로그인", description = "일반 로그인 입니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return ObjectResult.build(userService.userLogin(userLoginRequestDto));
    }

    @Operation(summary = "회원가입",description = "회원가입 입니다.")
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserJoinRequestDto userJoinRequestDto){
        return ObjectResult.build(userService.userJoin(userJoinRequestDto));
    }

    @Operation(summary = "accessToken 재발급", description = "refreshToken을 통해 accessToken을 재발급 합니다.")
    @PostMapping("/refresh/authorize")
    public ResponseEntity<?> refreshAuthorize(@Valid @RequestBody UserRefreshTokenRequestDto userRefreshTokenRequestDto) {
        return ObjectResult.build(userService.refreshAuthorize(userRefreshTokenRequestDto));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 입니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal ServiceUser serviceUser) {
        userService.logout(serviceUser);
        return ObjectResult.ok();
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 입니다.")
    @PostMapping("/drop")
    public ResponseEntity<?> dropUser(@AuthenticationPrincipal ServiceUser serviceUser) {
        userService.dropUser(serviceUser);
        return ObjectResult.ok();
    }

    @Operation(summary = "회원상태 체크", description = "회원상태 체크 입니다.")
    @GetMapping("/state/check")
    public ResponseEntity<?> userStateCheck(@AuthenticationPrincipal ServiceUser serviceUser) {
        return ObjectResult.build(userService.userStateCheck(serviceUser));
    }

    @Operation(summary = "즐겨찾기", description = "코인을 즐겨찾기를 합니다.")
    @PostMapping("/favorite")
    public ResponseEntity<?> addFavorite(@AuthenticationPrincipal ServiceUser serviceUser,
                                         @RequestBody AddFavoriteRequestDto addFavoriteRequestDto) {
        return ObjectResult.build(userService.addFavorite(serviceUser, addFavoriteRequestDto));
    }

    @Operation(summary = "즐겨찾기해제", description = "즐겨찾기 했던 항목을 해제 합니다.")
    @PutMapping("/favorite")
    public ResponseEntity<?> unFavorite(@AuthenticationPrincipal ServiceUser serviceUser,
                                        @RequestBody UnFavoriteRequestDto favoriteRequestDto) {
        userService.unFavorite(serviceUser, favoriteRequestDto);
        return ObjectResult.ok();
    }

    @Operation(summary = "즐겨찾기 목록 조회", description = "즐겨찾기 목록을 조회 합니다.")
    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal ServiceUser serviceUser) {
        return ListResult.build(userService.getFavorites(serviceUser));
    }

    @Operation(summary = "이용약관 목록 조회", description = "이용약관 목록을 조회 합니다.")
    @GetMapping("/useClauses")
    public ResponseEntity<?> getUseClauses() {
        return ObjectResult.build(userService.getUseClauses());
    }

    @Operation(summary = "앱 강제 업데이트 조회", description = "앱 버전을 통해 강제 업데이트 유무를 조회 합니다.")
    @GetMapping("/appVersion")
    public ResponseEntity<?> getAppVersion(@Parameter(description = "앱 OS", example = "01", required = true) @RequestParam String appOs,
                                           @Parameter(description = "앱 버전", example = "1.1.2", required = true) @RequestParam String appVersion) {
        userService.getAppVersion(appOs,appVersion);
        return ObjectResult.ok();
    }

    @Operation(summary = "화면 이동", description = "화면 이동시 해당 화면 방문을 저장 합니다.")
    @PutMapping("/move/view")
    public ResponseEntity<?> moveView(@AuthenticationPrincipal ServiceUser serviceUser,
                                      @Valid @RequestBody MoveViewRequestDto moveViewRequestDto) {
        userService.moveView(serviceUser, moveViewRequestDto);
        return ObjectResult.ok();
    }


}