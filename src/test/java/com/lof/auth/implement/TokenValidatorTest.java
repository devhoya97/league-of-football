package com.lof.auth.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lof.auth.domain.InvalidRefreshToken;
import com.lof.auth.domain.ValidRefreshToken;
import com.lof.auth.repository.InvalidRefreshTokenRepository;
import com.lof.auth.repository.ValidRefreshTokenRepository;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

class TokenValidatorTest {

    private TokenValidator tokenValidator;
    private ValidRefreshTokenRepository validRefreshTokenRepository;
    private InvalidRefreshTokenRepository invalidRefreshTokenRepository;
    private TokenParser tokenParser;

    @BeforeEach
    void init() {
        validRefreshTokenRepository = new FakeValidRefreshTokenRepository();
        invalidRefreshTokenRepository = new FakeInvalidRefreshTokenRepository();
        tokenParser = mock(TokenParser.class);
        tokenValidator = new TokenValidator(tokenParser, validRefreshTokenRepository, invalidRefreshTokenRepository);
    }

    @Test
    @DisplayName("ValidRefreshToken에 특정 회원의 refreshToken이 존재하면, 이를 InvalidRefreshRepository로 이동시킨다.")
    void invalidatePreviousRefreshToken() throws NoSuchFieldException, IllegalAccessException {
        // given
        long memberId = 1L;
        String refreshToken = "refreshToken";
        validRefreshTokenRepository.save(new ValidRefreshToken(memberId, refreshToken, 60L));
        when(tokenParser.parseExpirationSeconds(refreshToken)).thenReturn(60);

        // when
        tokenValidator.invalidatePreviousRefreshToken(memberId);

        // then
        assertAll(
                () -> assertThat(validRefreshTokenRepository.findById(memberId)).isEmpty(),
                () -> assertThat(invalidRefreshTokenRepository.findById(refreshToken).get()
                        .getMemberId()).isEqualTo(memberId)
        );
    }

    @Test
    @DisplayName("사용하려는 RefreshToken이 ValidRefreshTokenRepository에 존재하면서 InvalidRefreshTokenRepository에 존재하지 않으면 검증에 통과한다.")
    void validateRefreshToken() {
        // given
        long memberId = 1L;
        String refreshToken = "refreshToken";
        validRefreshTokenRepository.save(new ValidRefreshToken(memberId, refreshToken, 60L));

        // when & then
        assertThatCode(() -> tokenValidator.validateRefreshToken(memberId, "refreshToken"))
                .doesNotThrowAnyException();
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

    @Test
    @DisplayName("사용하려는 RefreshToken이 InvalidRefreshTokenRepository에 존재하면 예외가 발생한다.")
    void validateRefreshTokenByInvalidRefreshToken() {
        // given
        long memberId = 1L;
        String refreshToken = "refreshToken";
        validRefreshTokenRepository.save(new ValidRefreshToken(memberId, refreshToken, 60L));
        invalidRefreshTokenRepository.save(new InvalidRefreshToken(refreshToken, 1L, 60));

        // when & then
        assertThatThrownBy(() -> tokenValidator.validateRefreshToken(memberId, refreshToken))
                .isInstanceOf(BizException.class)
                .extracting((exception) -> ((BizException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_TOKEN);
    }
}
