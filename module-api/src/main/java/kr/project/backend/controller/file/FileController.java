package kr.project.backend.controller.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import kr.project.backend.common.Environment;
import kr.project.backend.results.ObjectResult;
import kr.project.backend.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Tag(name = "파일", description = "파일 처리")
@Slf4j
@RestController
@RequestMapping("/api/" + Environment.API_VERSION + "/" + Environment.API_ADMIN + "/file")
@RequiredArgsConstructor
public class FileController {

    final private FileService fileService;

    @Operation(summary = "이미지 업로드",description = "이미지를 업로드 합니다.")
    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
        fileService.uploadImage(multipartHttpServletRequest);
        return ObjectResult.ok();
    }

    @Operation(summary = "이미지 열기",description = "url 입력시 해당 이미지를 노출합니다.")
    @GetMapping("/show/image/{fileId}")
    public ResponseEntity<byte[]> showImage(@Parameter(name = "fileId", description = "파일ID", example = "6j4trvC7ac")
                                       @PathVariable(name = "fileId") String fileId) throws Exception, IOException{ 
        return fileService.showImage(fileId);
    }
    
}