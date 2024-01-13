package kr.project.backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.project.backend.auth.ServiceUser;
import kr.project.backend.utils.HttpRequestDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponsBodyOutPutLoggingFilter implements ResponseBodyAdvice {
    private ObjectMapper mapper = new ObjectMapper();

    private int MAX_RESULT_SIZE = 3998;

    @Override
    public boolean supports (MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite (Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        final ServletRequestAttributes servletRequestAttributes =   (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final HttpServletRequest httpRequest = servletRequestAttributes.getRequest();
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) httpRequest;

        final String uri = httpRequest.getRequestURI();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(body != null) {

            try {
                String logId = (String) servletRequestAttributes.getRequest().getAttribute("transaction_id");
                String result =  mapper.writeValueAsString(body);
                String userId =null;
                if( auth != null  && auth.getPrincipal()  != null  ) {
                    if( auth.getPrincipal() instanceof ServiceUser) {
                        ServiceUser serviceUser = ( ServiceUser ) auth.getPrincipal();
                        userId = serviceUser.getUserId();
                    }else{
                        userId = "security 해제로 인한 no serviceUser";
                    }
                }

                //DB 컬럼 길이보다 데이터가 큰경우 최대 길이 만큼 잘라서 DB 저장
                byte[] payload=  result.getBytes(StandardCharsets.UTF_8);
                int length = Math.min(payload.length, MAX_RESULT_SIZE);

                try {
                    result = new String(payload, 0, length, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    log.error("result data maxlength change error" ,e);
                }

                log.info("----> request Header ::: "+ HttpRequestDataUtil.requestHeaderData(httpRequest));
                log.info("----> request param ::: "+ mapper.readTree(cachingRequest.getContentAsByteArray()));
                log.info("----> request logId::: "+logId);
                log.info("----> request userId::: "+userId);
                log.info("----> request uri::: "+uri);
                log.info("----> request client ip::: "+httpRequest.getRemoteAddr());
                log.info("<---- response ::: "+result);

                //TODO inout 데이터 저장 필요

            }catch ( Exception e) {
                log.error("saveInoutLog insert error ",e);
            }
        }
        return body;
    }
}
