package com.lof.auth.implement;

import org.springframework.stereotype.Component;

import com.lof.auth.domain.InvalidRefreshToken;
import com.lof.auth.repository.InvalidRefreshTokenRepository;
import com.lof.auth.repository.ValidRefreshTokenRepository;
import com.lof.global.exception.AuthException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final ValidRefreshTokenRepository validRefreshTokenRepository;
    private final InvalidRefreshTokenRepository invalidRefreshTokenRepository;
    private final TokenManager tokenManager;

    public void invalidatePreviousRefreshToken(Member member) {
        validRefreshTokenRepository.findById(member.getId())
                .ifPresent((validRefreshToken) -> {
                    validRefreshTokenRepository.deleteById(member.getId());
                    int remainingExpiration = tokenManager.parseExpirationSeconds(validRefreshToken.getRefreshToken());
                    invalidRefreshTokenRepository.save(new InvalidRefreshToken(validRefreshToken, remainingExpiration));
                });
    }

    public void validateRefreshToken(String refreshToken) {
        invalidRefreshTokenRepository.findById(refreshToken)
                .ifPresent((invalidRefreshToken) -> {
                    throw new AuthException(ErrorCode.INVALID_TOKEN);
                });
    }
}
