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
    private final TokenManager tokenManager; // 순환참조 가능성 때문에 같은 layer끼리 의존하는건 피하려했는데, 그럼 서비스 로직이 너무 길어짐

    /*
    Repository(Redis)를 mocking해서 테스트하려면 어떻게 해야하지?
    stub으로 해야하나?
     */
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
