package com.lof.auth.implement;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.lof.auth.domain.InvalidRefreshToken;
import com.lof.auth.domain.ValidRefreshToken;
import com.lof.auth.repository.InvalidRefreshTokenRepository;
import com.lof.auth.repository.ValidRefreshTokenRepository;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final TokenParser tokenParser;
    private final ValidRefreshTokenRepository validRefreshTokenRepository;
    private final InvalidRefreshTokenRepository invalidRefreshTokenRepository;

    /*
    Repository(Redis)를 mocking해서 테스트하려면 어떻게 해야하지?
    stub으로 해야하나?
     */
    public void saveValidRefreshToken(long memberId, String refreshToken, long expiration) {
        validRefreshTokenRepository.save(new ValidRefreshToken(memberId, refreshToken, expiration));
    }

    public void invalidatePreviousRefreshToken(long memberId) {
        validRefreshTokenRepository.findById(memberId)
                .ifPresent((validRefreshToken) -> {
                    validRefreshTokenRepository.deleteById(memberId);
                    int remainingExpiration = tokenParser.parseExpirationSeconds(validRefreshToken.getRefreshToken());
                    invalidRefreshTokenRepository.save(new InvalidRefreshToken(validRefreshToken, remainingExpiration));
                });
    }

    public void validateRefreshToken(long memberId, String refreshToken) {
        ValidRefreshToken validRefreshToken = validRefreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new BizException(ErrorCode.INVALID_TOKEN));

        if (!Objects.equals(validRefreshToken.getRefreshToken(), refreshToken)) {
            throw new BizException(ErrorCode.INVALID_TOKEN);
        }

        invalidRefreshTokenRepository.findById(refreshToken)
                .ifPresent((invalidRefreshToken) -> {
                    throw new BizException(ErrorCode.INVALID_TOKEN);
                });
    }
}
