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

        // authInterceptor에서 예외가 발생하면, 여기서 처리
        BizException e = (BizException) request.getAttribute("error");
        if (e != null) {
            log.error("method: {}, URI: {}, code: {}", request.getMethod(), request.getRequestURI(), e.getCode(), e);
            throw e;
        }

        String requestId = (String) UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);
        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/signup")) {
            // username까지는 찍을 수 있으면 좋겠다.
            log.info("requestId: {}, method: {}, URI: {}", requestId, request.getMethod(), request.getRequestURI());
            return true;
        }

        long memberId = (long) request.getAttribute("memberId");
        // TODO: requestBody 찍기
        log.info("requestId: {}, method: {}, URI: {}, memberId = {}", requestId, request.getMethod(), request.getRequestURI(), memberId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestId = (String) request.getAttribute("requestId");
        // globalExceptionHandler가 던져준 예외는 여기서 처리
        BizException e = (BizException) request.getAttribute("error");
        if (e != null) {
            log.error("requestId: {}, method: {}, URI: {}, code: {}", requestId, request.getMethod(), request.getRequestURI(), e.getCode(), e);
            return;
        }

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login") || requestURI.equals("/signup")) {
            long startTime = (long) request.getAttribute("startTime");
            long duration = System.currentTimeMillis() - startTime;
            log.info("requestId: {}, responseStatus: {}, duration: {}", requestId, response.getStatus(), duration);
            return;
        }

        long memberId = (long) request.getAttribute("memberId");

        // TODO: responseBody 찍기
        //  단 login-refresh의 경우 memberId는 찍어도 되는데 responseBody는 찍으면 안 된다. 토큰을 로깅하면 안되니까
        long startTime = (long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        log.info("requestId: {}, responseStatus: {}, memberId: {}, duration: {}", requestId, response.getStatus(), memberId, duration);
    }
}
