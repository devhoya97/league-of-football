package com.lof.auth.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lof.auth.domain.InvalidRefreshToken;
import com.lof.auth.domain.ValidRefreshToken;
import com.lof.auth.repository.InvalidRefreshTokenRepository;
import com.lof.auth.repository.ValidRefreshTokenRepository;
import com.lof.global.exception.AuthException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.fixture.MemberFixture;

class TokenValidatorTest {

    private TokenValidator tokenValidator;
    private ValidRefreshTokenRepository validRefreshTokenRepository;
    private InvalidRefreshTokenRepository invalidRefreshTokenRepository;
    private TokenManager tokenManager;

    @BeforeEach
    void init() {
        validRefreshTokenRepository = new FakeValidRefreshTokenRepository();
        invalidRefreshTokenRepository = new FakeInvalidRefreshTokenRepository();
        tokenManager = mock(TokenManager.class);
        tokenValidator = new TokenValidator(validRefreshTokenRepository, invalidRefreshTokenRepository, tokenManager);
    }

    @Test
    @DisplayName("ValidRefreshToken에 특정 회원의 refreshToken이 존재하면, 이를 InvalidRefreshRepository로 이동시킨다.")
    void invalidatePreviousRefreshToken() throws NoSuchFieldException, IllegalAccessException {
        // given
        long memberId = 1L;
        String refreshToken = "refreshToken";
        Member member = MemberFixture.createMember(memberId, "username", "password");
        validRefreshTokenRepository.save(new ValidRefreshToken(memberId, refreshToken, 60L));
        when(tokenManager.parseExpirationSeconds(refreshToken)).thenReturn(60);

        // when
        tokenValidator.invalidatePreviousRefreshToken(member);

        // then
        assertAll(
                () -> assertThat(validRefreshTokenRepository.findById(memberId)).isEmpty(),
                () -> assertThat(invalidRefreshTokenRepository.findById(refreshToken).get().getMemberId()).isEqualTo(memberId)
        );
    }

    @Test
    @DisplayName("사용하려는 RefreshToken이 InvalidRefreshTokenRepository에 존재하지 않으면 검증에 통과한다.")
    void validateRefreshToken() {
        assertThatCode(() -> tokenValidator.validateRefreshToken("refreshToken"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("사용하려는 RefreshToken이 InvalidRefreshTokenRepository에 존재하면 예외가 발생한다.")
    void validateRefreshTokenThrowException() {
        // given
        String refreshToken = "refreshToken";
        invalidRefreshTokenRepository.save(new InvalidRefreshToken(refreshToken, 1L, 60));

        // when & then
        assertThatThrownBy(() -> tokenValidator.validateRefreshToken(refreshToken))
                .isInstanceOf(AuthException.class)
                .extracting((exception) -> ((AuthException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_TOKEN);
    }
}
