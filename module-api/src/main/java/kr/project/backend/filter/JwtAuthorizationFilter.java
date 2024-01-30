package kr.project.backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.project.backend.auth.ServiceUser;
import kr.project.backend.utils.AesUtil;
import kr.project.backend.utils.JwtUtil;
import kr.project.backend.common.ApiResponseMessage;
import kr.project.backend.common.CommonErrorCode;
import kr.project.backend.common.CommonException;
import kr.project.backend.common.Environment;
import kr.project.backend.entity.user.User;
import kr.project.backend.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;


@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final AesUtil aesUtil;
    private final String TOKEN_PREFIX = "Bearer ";
    private final String ADMIN_REQUEST_URL = "/api/" + Environment.API_VERSION + "/" + Environment.API_ADMIN;

    private final String USER_REQUEST_URL = "/api/" + Environment.API_VERSION + "/" + Environment.API_USER;

    private final String COMMON_REQUEST_URL = "/api/" + Environment.API_VERSION + "/" + Environment.API_COMMON;
    private final String ADMIN_EMAIL = "ADMIN@staking.com";
    private final String ADMIN = "ADMIN";
    private final String USER = "USER";

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${admin.aesKey}")
    private String adminAESKey;

    @Value("${admin.aesIv}")
    private String adminAESIv;

    @Value("${admin.apiKeyName}")
    private String adminApiKeyName;

    public JwtAuthorizationFilter(UserRepository userRepository, AesUtil aesUtil) {
        this.userRepository = userRepository;
        this.aesUtil = aesUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            String headerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = null;

            //header에 인증 token 검사
            if (StringUtils.isEmpty(headerToken)) {
                chain.doFilter(request, response);
                return;
            } else {
                token = headerToken.substring(TOKEN_PREFIX.length());
            }

            //token 사용자, 관리자 분기
            String requestUrl = request.getRequestURI();

            String simpleGrantedAuthority = "";

            ServiceUser serviceUser = new ServiceUser();

            //관리자
            if (requestUrl.startsWith(ADMIN_REQUEST_URL)) {

                //apiKey 검사
                String adminCheck = aesUtil.decryptAES256(adminAESKey, adminAESIv, token);

                if (adminCheck.startsWith(adminApiKeyName)) {
                    serviceUser = new ServiceUser();
                    serviceUser.setUserEmail(ADMIN_EMAIL);
                } else {
                    throw new CommonException(CommonErrorCode.WRONG_TOKEN.getCode(), CommonErrorCode.WRONG_TOKEN.getMessage());
                }

                simpleGrantedAuthority = ADMIN;

            //사용자
            } else if(requestUrl.startsWith(USER_REQUEST_URL)) {

                //jwt 유효성검사
                if (JwtUtil.isExpired(token, jwtSecretKey)) {
                    chain.doFilter(request, response);
                    return;
                }

                //jwt decode
                serviceUser = JwtUtil.decode(token, jwtSecretKey);

                User userInfo = userRepository.findById(serviceUser.getUserId())
                        .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

                simpleGrantedAuthority = USER;

            //공통(사용자, 관리자 모두 사용)
            } else {

                //사용자 검사
                if(token.length() > 100){
                    //jwt 유효성검사
                    if (JwtUtil.isExpired(token, jwtSecretKey)) {
                        chain.doFilter(request, response);
                        return;
                    }

                    //jwt decode
                    serviceUser = JwtUtil.decode(token, jwtSecretKey);

                    User userInfo = userRepository.findById(serviceUser.getUserId())
                            .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND_USER.getCode(), CommonErrorCode.NOT_FOUND_USER.getMessage()));

                    simpleGrantedAuthority = USER;

                //관리자 검사
                }else{
                    //apiKey 검사
                    String adminCheck = aesUtil.decryptAES256(adminAESKey, adminAESIv, token);

                    if (adminCheck.startsWith(adminApiKeyName)) {
                        serviceUser = new ServiceUser();
                        serviceUser.setUserEmail(ADMIN_EMAIL);
                    } else {
                        throw new CommonException(CommonErrorCode.WRONG_TOKEN.getCode(), CommonErrorCode.WRONG_TOKEN.getMessage());
                    }

                    simpleGrantedAuthority = ADMIN;
                }
            }

            //인증
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(serviceUser, null,
                    Collections.singletonList(new SimpleGrantedAuthority(simpleGrantedAuthority)));

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authenticationToken);

        } catch (ExpiredJwtException e) {

            ApiResponseMessage apiResponseMessage = new ApiResponseMessage();
            apiResponseMessage.setStatus(CommonErrorCode.FAIL.getCode());
            apiResponseMessage.setMessage(CommonErrorCode.FAIL.getMessage());
            apiResponseMessage.setErrorCode(CommonErrorCode.EXPIRED_TOKEN.getCode());
            apiResponseMessage.setErrorMessage(CommonErrorCode.EXPIRED_TOKEN.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(objectMapper.writeValueAsString(apiResponseMessage));
            response.flushBuffer();

            return;

        } catch (CommonException e) {

            ApiResponseMessage apiResponseMessage = new ApiResponseMessage();
            apiResponseMessage.setStatus(CommonErrorCode.FAIL.getCode());
            apiResponseMessage.setMessage(CommonErrorCode.FAIL.getMessage());
            apiResponseMessage.setErrorCode(e.getCode());
            apiResponseMessage.setErrorMessage(e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(objectMapper.writeValueAsString(apiResponseMessage));
            response.flushBuffer();

            return;

        } catch (IllegalArgumentException e) {

            ApiResponseMessage apiResponseMessage = new ApiResponseMessage();
            apiResponseMessage.setStatus(CommonErrorCode.FAIL.getCode());
            apiResponseMessage.setMessage(CommonErrorCode.FAIL.getMessage());
            apiResponseMessage.setErrorCode(CommonErrorCode.WRONG_TOKEN.getCode());
            apiResponseMessage.setErrorMessage(e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(objectMapper.writeValueAsString(apiResponseMessage));
            response.flushBuffer();

            return;

        }catch (IllegalBlockSizeException e){

            ApiResponseMessage apiResponseMessage = new ApiResponseMessage();
            apiResponseMessage.setStatus(CommonErrorCode.FAIL.getCode());
            apiResponseMessage.setMessage(CommonErrorCode.FAIL.getMessage());
            apiResponseMessage.setErrorCode(CommonErrorCode.WRONG_TOKEN.getCode());
            apiResponseMessage.setErrorMessage(e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(objectMapper.writeValueAsString(apiResponseMessage));
            response.flushBuffer();

            return;

        }catch (Exception e){

            ApiResponseMessage apiResponseMessage = new ApiResponseMessage();
            apiResponseMessage.setStatus(CommonErrorCode.FAIL.getCode());
            apiResponseMessage.setMessage(CommonErrorCode.FAIL.getMessage());
            apiResponseMessage.setErrorCode(CommonErrorCode.COMMON_FAIL.getCode());
            apiResponseMessage.setErrorMessage(CommonErrorCode.COMMON_FAIL.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(objectMapper.writeValueAsString(apiResponseMessage));
            response.flushBuffer();

            return;

        }

        chain.doFilter(request,response);
    }
}
