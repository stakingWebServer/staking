package kr.project.backend.controller.coin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import kr.project.backend.auth.ServiceUser;
import kr.project.backend.results.ListResult;
import kr.project.backend.service.coin.StakingInfoService;
import kr.project.backend.common.Environment;
import kr.project.backend.results.ObjectResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "staking", description = "스테이킹")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/" + Environment.API_VERSION + "/" + Environment.API_USER + "/staking")
public class StakingInfoController {

    private final StakingInfoService stakingInfoService;

    @Operation(summary = "코인 스테이킹 목록 조회", description = "코인 스테이킹 목록 조회를 합니다.")
    @GetMapping("/infos-all")
    public ResponseEntity<?> stakingInfosAll(@AuthenticationPrincipal ServiceUser serviceUser) {
        return ObjectResult.build(stakingInfoService.getStakingInfosAll(serviceUser));
    }

    @Operation(summary = "코인 스테이킹 상세 조회", description = "코인 스테이킹 상세 조회를 합니다.")
    @GetMapping("/info/{stakingId}")
    public ResponseEntity<?> stakingInfo(@PathVariable(value = "stakingId") String stakingId) {
        return ObjectResult.build(stakingInfoService.getStakingInfo(stakingId));
    }
}
