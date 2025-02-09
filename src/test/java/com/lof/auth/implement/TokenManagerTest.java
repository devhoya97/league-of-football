package com.lof.auth.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

import com.lof.auth.domain.LoginToken;
import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.fixture.MemberFixture;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
class TokenManagerTest {

    private static final long ACCESS_TOKEN_EXPIRATION = 1000L;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L;

    @Autowired
    private EntityManager em;

    private final TokenManager tokenManager = new TokenManager("thisIslongEnoughTestSecretKeyForJWTHMACSHAalgorithm", ACCESS_TOKEN_EXPIRATION, REFRESH_TOKEN_EXPIRATION);

    @Test
    @DisplayName("같은 회원의 토큰을 발급하더라도, 발급한 시간이 다르면 토큰의 값은 서로 다르다.")
    void createToken() throws InterruptedException {
        // given
        Member member = MemberFixture.createMember("memberA", "1234");
        em.persist(member);
        LoginToken token1 = tokenManager.createLoginToken(member);

        // when
        Thread.sleep(1000);
        LoginToken token2 = tokenManager.createLoginToken(member);

        // then
        assertThat(token1.equals(token2)).isFalse();
    }

    @Test
    @DisplayName("토큰으로부터 회원엔티티의 id값을 파싱할 수 있다.")
    void parseMemberId() {
        // given
        Member member = MemberFixture.createMember("memberA", "1234");
        em.persist(member);
        LoginToken token = tokenManager.createLoginToken(member);

        // when
        long memberId = tokenManager.parseMemberId(token.accessToken());

        // then
        assertThat(memberId).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로부터 회원엔티티의 id값을 파싱하려고 시도하면 예외가 발생한다.")
    void parseMemberIdInvalidToken() {
        // given
        String token = "invalidToken";

        // when & then
        assertThatThrownBy(() -> tokenManager.parseMemberId(token))
                .isInstanceOf(BadRequestException.class)
                .extracting(exception -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_TOKEN);
    }

    @Test
    @DisplayName("만료 기한이 지난 토큰으로부터 회원엔티티의 id값을 파싱하려고 시도하면 예외가 발생한다.")
    void parseMemberIdExpiredToken() throws InterruptedException {
        // given
        Member member = MemberFixture.createMember("memberA", "1234");
        em.persist(member);
        LoginToken token = tokenManager.createLoginToken(member);
        Thread.sleep(ACCESS_TOKEN_EXPIRATION * 2);

        // when & then
        assertThatThrownBy(() -> tokenManager.parseMemberId(token.accessToken()))
                .isInstanceOf(BadRequestException.class)
                .extracting(exception -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.EXPIRED_TOKEN);
    }
}
