package kr.project.backend.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.project.backend.results.ListResult;
import kr.project.backend.service.file.FileService;
import kr.project.backend.common.Environment;
import kr.project.backend.results.ObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;


@Tag(name = "파일", description = "파일 처리")
@Slf4j
@RestController
@RequestMapping("/api/" + Environment.API_VERSION + "/" + Environment.API_COMMON + "/file")
@RequiredArgsConstructor
public class FileController {

    final private FileService fileService;

    @Operation(summary = "이미지 업로드",description = "이미지를 업로드 합니다.")
    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
        return ListResult.build(fileService.uploadImage(multipartHttpServletRequest));
    }

    @Operation(summary = "이미지 열기",description = "url 입력시 해당 이미지를 노출합니다.")
    @GetMapping("/image/{fileId}")
    public ResponseEntity<byte[]> showImage(@Parameter(name = "fileId", description = "파일ID", example = "6j4trvC7ac")
                                       @PathVariable(name = "fileId") String fileId) throws Exception, IOException{
        return fileService.showImage(fileId);
    }

    @Operation(summary = "개인정보 이용약관", description = "현재 적용중인 개인정보 이용약관을 조회 합니다.")
    @GetMapping("/privacy-clause")
    public ResponseEntity<byte[]> getPrivacyClause() throws Exception {
        return fileService.getPrivacyClause();
    }

}
