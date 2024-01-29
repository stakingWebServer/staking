package kr.project.backend.service.admin;

import kr.project.backend.dto.admin.response.TodayDropUserResponseDto;
import kr.project.backend.dto.admin.response.TodayLoginUserResponseDto;
import kr.project.backend.dto.admin.response.TodayRegisterResponseDto;
import kr.project.backend.repository.user.DropUserRepository;
import kr.project.backend.repository.user.UserRepository;
import kr.project.backend.dto.admin.response.AccessKeyResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static kr.project.backend.utils.AesUtil.encryptAES256;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    @Value("${admin.aesKey}")
    private String adminAESKey;

    @Value("${admin.aesIv}")
    private String adminAESIv;

    private final UserRepository userRepository;
    private final DropUserRepository dropUserRepository;

    public AccessKeyResponseDto giveApikey(String plainText) throws Exception{
        return new AccessKeyResponseDto(encryptAES256(adminAESKey, adminAESIv, plainText+System.currentTimeMillis()));
    }

    @Transactional(readOnly = true)
    public TodayRegisterResponseDto getTodayRegister() {
        int todayRegister =  userRepository.countByCreatedDateBetween(
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new TodayRegisterResponseDto(todayRegister);
    }

    @Transactional(readOnly = true)
    public TodayLoginUserResponseDto getTodayLoginUser() {
        int todayLoginUser = userRepository.countByUserLoginDttmBetween(
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new TodayLoginUserResponseDto(todayLoginUser);
    }

    public TodayDropUserResponseDto getTodayDropUser() {
        int todayDropUser = dropUserRepository.countByDropDttmBetween(
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new TodayDropUserResponseDto(todayDropUser);
    }
}
