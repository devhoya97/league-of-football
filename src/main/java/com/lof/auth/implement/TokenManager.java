package com.lof.auth.implement;

import org.springframework.stereotype.Component;

import com.lof.auth.implement.dto.LoginToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final TokenValidator tokenValidator;
    private final TokenIssuer tokenIssuer;
    private final TokenParser tokenParser;

    // refreshToken으로 memberId를 파싱하는 경우는, 토큰 재발급 외엔 없다.
    public LoginToken reissueLoginToken(String refreshToken) {
        tokenValidator.validateRefreshToken(refreshToken);
        long memberId = tokenParser.parseMemberId(refreshToken);

        return createLoginToken(memberId);
    }

    public LoginToken createLoginToken(long memberId) {
        tokenValidator.invalidatePreviousRefreshToken(memberId);

        String accessToken = tokenIssuer.createAccessToken(memberId);
        String refreshToken = tokenIssuer.createRefreshToken(memberId);

        return new LoginToken(accessToken, refreshToken);
    }

    // refreshToken으로부터 memberId를 파싱하려면, tokenValidator.validateRefreshToken(refreshToken)을 꼭 거쳐야 함.
    public long parseAccessTokenToMemberId(String accessToken) {
        return tokenParser.parseMemberId(accessToken);
    }
}
