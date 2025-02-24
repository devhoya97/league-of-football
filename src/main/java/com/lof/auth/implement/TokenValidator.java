package com.lof.auth.implement;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.lof.auth.domain.ValidRefreshToken;
import com.lof.auth.repository.ValidRefreshTokenRepository;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final ValidRefreshTokenRepository validRefreshTokenRepository;

    public void saveValidRefreshToken(long memberId, String refreshToken) {
        validRefreshTokenRepository.save(new ValidRefreshToken(memberId, refreshToken));
    }

    public void invalidatePreviousRefreshToken(long memberId, String refreshToken) {
        validateRefreshToken(memberId, refreshToken);
        validRefreshTokenRepository.deleteById(memberId);
    }

    public void validateRefreshToken(long memberId, String refreshToken) {
        ValidRefreshToken validRefreshToken = validRefreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new BizException(ErrorCode.INVALID_TOKEN));

        if (!Objects.equals(validRefreshToken.getRefreshToken(), refreshToken)) {
            throw new BizException(ErrorCode.INVALID_TOKEN);
        }
    }
}
