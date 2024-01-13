package kr.project.backend.service.admin;

import kr.project.backend.dto.admin.response.AccessKeyResponseDto;
import kr.project.backend.utils.AesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    @Value("${admin.aesKey}")
    private String adminAESKey;

    @Value("${admin.aesIv}")
    private String adminAESIv;

    private final AesUtil aesUtil;
    
    @Transactional(readOnly = true)
    public AccessKeyResponseDto adminService(String plainText) throws Exception{
        String accessKey = aesUtil.encryptAES256(adminAESKey, adminAESIv, plainText+System.currentTimeMillis());
        return new AccessKeyResponseDto(accessKey);
    }
}
