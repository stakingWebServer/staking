package kr.project.backend.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequestDataUtil {

    public static String requestHeaderData(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            sb.append(headerName).append("=").append(headerValue).append(", ");
        }
        return sb.toString();
    }

    public static String requestFormData(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        StringBuilder sb = new StringBuilder();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            sb.append(paramName).append("=").append(paramValue).append("&");
        }
        return sb.toString();
    }

}
