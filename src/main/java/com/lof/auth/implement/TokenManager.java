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
        LoginToken loginToken = tokenIssuer.createLoginToken(memberId);
        tokenValidator.saveValidRefreshToken(memberId, loginToken.refreshToken());

        return loginToken;
    }

    public void invalidateRefreshToken(long memberId, String refreshToken) {
        tokenValidator.invalidatePreviousRefreshToken(memberId, refreshToken);
    }
}
