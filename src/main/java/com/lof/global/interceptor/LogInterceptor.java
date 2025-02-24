package com.lof.global.interceptor;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.lof.global.exception.BizException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);

        // TODO: requestBody 찍기 (/login 제외)
        log.info("requestId: {}, method: {}, URI: {}", requestId, request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestId = (String) request.getAttribute("requestId");
        Long memberId = (Long) request.getAttribute("memberId");
        long startTime = (long) request.getAttribute("startTime");

        // GlobalExceptionHandler가 넘겨준 예외를 여기서 처리
        BizException e = (BizException) request.getAttribute("error");
        if (e != null) {
            log.error("requestId: {}, responseStatus: {}, memberId: {}, duration: {}, code: {}", requestId, response.getStatus(), memberId, System.currentTimeMillis() - startTime, e.getCode(), e);
            return;
        }

        // TODO: responseBody 찍기 (/login, /login-refresh 제외)
        log.info("requestId: {}, responseStatus: {}, memberId: {}, duration: {}", requestId, response.getStatus(), memberId, System.currentTimeMillis() - startTime);
    }
}
