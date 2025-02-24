package com.lof.auth.implement;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lof.auth.domain.ValidRefreshToken;
import com.lof.auth.repository.ValidRefreshTokenRepository;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

class TokenValidatorTest {

    private TokenValidator tokenValidator;
    private ValidRefreshTokenRepository validRefreshTokenRepository;

    @BeforeEach
    void init() {
        validRefreshTokenRepository = new FakeValidRefreshTokenRepository();
        tokenValidator = new TokenValidator(validRefreshTokenRepository);
    }

    @Test
    @DisplayName("사용하려는 RefreshToken이 ValidRefreshTokenRepository에 존재하면 검증에 통과한다.")
    void validateRefreshToken() {
        // given
        long memberId = 1L;
        String refreshToken = "refreshToken";
        validRefreshTokenRepository.save(new ValidRefreshToken(memberId, refreshToken));

        // when & then
        assertThatCode(() -> tokenValidator.validateRefreshToken(memberId, "refreshToken"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("사용하려는 RefreshToken이 ValidRefreshTokenRepository에 저장된 값과 다르면 예외가 발생한다.")
    void validateRefreshTokenNotSame() {
        // given
        long memberId = 1L;
        String refreshToken = "refreshToken";
        validRefreshTokenRepository.save(new ValidRefreshToken(memberId, "NotSame" + refreshToken));

        // when & then
        assertThatThrownBy(() -> tokenValidator.validateRefreshToken(memberId, refreshToken))
                .isInstanceOf(BizException.class)
                .extracting((exception) -> ((BizException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_TOKEN);
    }

    @Test
    @DisplayName("사용하려는 RefreshToken이 ValidRefreshTokenRepository에 존재하지 않으면 예외가 발생한다.")
    void validateRefreshTokenNotExists() {
        // given
        long memberId = 1L;
        String refreshToken = "refreshToken";

        // when & then
        assertThatThrownBy(() -> tokenValidator.validateRefreshToken(memberId, refreshToken))
                .isInstanceOf(BizException.class)
                .extracting((exception) -> ((BizException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_TOKEN);
    }
}
