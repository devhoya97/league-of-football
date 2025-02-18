package com.lof.auth.implement;

import org.springframework.stereotype.Component;

import com.lof.auth.implement.dto.LoginToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final TokenValidator tokenValidator;
    private final TokenIssuer tokenIssuer;

    public LoginToken reissueLoginToken(long memberId, String refreshToken) {
        tokenValidator.validateRefreshToken(memberId, refreshToken);

        return createLoginToken(memberId);
    }

    public LoginToken createLoginToken(long memberId) {
        tokenValidator.invalidatePreviousRefreshToken(memberId);

        String accessToken = tokenIssuer.createAccessToken(memberId);
        String refreshToken = tokenIssuer.createRefreshToken(memberId);
        tokenValidator.saveValidRefreshToken(memberId, refreshToken, tokenIssuer.getRefreshTokenExpiration());

        return new LoginToken(accessToken, refreshToken);
    }
}
