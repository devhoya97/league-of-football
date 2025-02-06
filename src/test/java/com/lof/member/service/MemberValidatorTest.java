package com.lof.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;

class MemberValidatorTest {

    private MemberValidator memberValidator = new MemberValidator();

    @Test
    @DisplayName("유효한 아이디, 비밀번호는 검증을 통과한다.")
    void validate() {
        assertDoesNotThrow(() -> memberValidator.validate(new Member("user1", "Password1!")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "abcd", "thisisaverylonguserna", "user@name", "user name"})
    @DisplayName("잘못된 아이디는 예외를 발생시켜야 한다.")
    void validateInvalidLoginId(String loginId) {
        Member member = new Member(loginId, "Password1!");
        assertThatThrownBy(() -> memberValidator.validate(member))
                .isInstanceOf(BadRequestException.class)
                .extracting(exception -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_LOGINID);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Short2!", "TooLongPassword12345!", "nobigalpha1!", "NOSMALLALPHA1!", "NoSpecialSymbol1"})
    @DisplayName("잘못된 비밀번호는 예외를 발생시켜야 한다.")
    void validateInvalidPassword(String password) {
        Member member = new Member("user1", password);
        assertThatThrownBy(() -> memberValidator.validate(member))
                .isInstanceOf(BadRequestException.class)
                .extracting(exception -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_PASSWORD);
    }
}
