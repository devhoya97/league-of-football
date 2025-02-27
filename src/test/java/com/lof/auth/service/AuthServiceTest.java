package com.lof.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.fixture.MemberFixture;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("회원 이름이 중복된 회원 생성을 시도하면 예외가 발생한다.")
    void signUp() {
        // given
        Member member1 = MemberFixture.createMember("sameUsername", "password1");
        Member member2 = MemberFixture.createMember("sameUsername", "password2");
        authService.signUp(member1);

        // when & then
        assertThatThrownBy(() -> authService.signUp(member2))
                .isInstanceOf(BizException.class)
                .extracting(exception -> ((BizException) exception).getCode())
                .isEqualTo(ErrorCode.DUPLICATED_USERNAME);
    }
}
