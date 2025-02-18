package com.lof.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lof.auth.implement.TokenParser;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenParser tokenParser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (refreshToken == null) {
            throw new BizException(ErrorCode.MISSING_TOKEN);
        }
        long memberId = tokenParser.parseMemberId(refreshToken);
        request.setAttribute("memberId", memberId);

        return true;
    }
}
