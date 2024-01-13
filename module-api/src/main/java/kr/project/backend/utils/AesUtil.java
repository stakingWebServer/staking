package kr.project.backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class AesUtil {

    public static String algorithms = "AES/CBC/PKCS5Padding";

    public static String encryptAES256(String adminAESKey, String adminAESIv, String planinText) throws Exception{

        //암호화/복호화 기능이 포함된 객체 생성
        Cipher cipher = Cipher.getInstance(algorithms);

        //키로 비밀키 생성
        SecretKeySpec keySpec = new SecretKeySpec(adminAESKey.getBytes(), "AES");

        //iv 로 spec 생성
        IvParameterSpec ivParamSpec = new IvParameterSpec(adminAESIv.getBytes());

        //암호화 적용
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        //암호화 실행
        byte[] encrypted = cipher.doFinal(planinText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encrypted);
    }


    public static String decryptAES256(String adminAESKey, String adminAESIv, String cipherText) throws Exception{
        //암호화/복호화 기능이 포함된 객체 생성
        Cipher cipher = Cipher.getInstance(algorithms);

        //키로 비밀키 생성
        SecretKeySpec keySpec = new SecretKeySpec(adminAESKey.getBytes(), "AES");

        //iv 로 spec 생성
        IvParameterSpec ivParamSpec = new IvParameterSpec(adminAESIv.getBytes());

        //암호화 적용
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        //암호 해석
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
