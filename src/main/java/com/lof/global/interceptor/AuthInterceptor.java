package com.lof.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lof.auth.implement.TokenParser;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

// 여기서 memberId를 setAttribute 해주면 LogInterceptor가 사용할 수 있도록 한다.
// LogInterceptor는 memberId가 있어야 하는데 없는 경우, 예외 상황으로 판단하고 그에 맞는 로그를 찍는다.
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenParser tokenParser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (refreshToken == null) {
                throw new BizException(ErrorCode.MISSING_TOKEN);
            }
            long memberId = tokenParser.parseMemberId(refreshToken);
            request.setAttribute("memberId", memberId);
        } catch (BizException e) {
            request.setAttribute("error", e);
        }
        return true;
    }
}
