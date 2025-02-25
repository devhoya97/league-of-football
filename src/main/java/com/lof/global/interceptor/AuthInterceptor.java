package com.lof.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lof.auth.domain.TokenType;
import com.lof.auth.implement.TokenParser;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenParser tokenParser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = getAccessToken(request);
        validateIsAccessToken(accessToken);
        long memberId = tokenParser.parseMemberId(accessToken);
        request.setAttribute("memberId", memberId);

        return true;
    }

    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken == null) {
            throw new BizException(ErrorCode.MISSING_TOKEN);
        }
        return accessToken;
    }

    private void validateIsAccessToken(String accessToken) {
        if (tokenParser.parseTokenType(accessToken) != TokenType.ACCESS) {
            throw new BizException(ErrorCode.ACCESS_TOKEN_REQUIRED);
        }
    }
}
