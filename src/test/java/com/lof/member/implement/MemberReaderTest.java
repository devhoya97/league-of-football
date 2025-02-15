package com.lof.member.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.fixture.MemberFixture;
import com.lof.member.repository.MemberRepository;

class MemberReaderTest {

    private MemberReader memberReader;

    private MemberRepository memberRepository;

    @BeforeEach
    void init() {
        memberRepository = mock(MemberRepository.class);
        memberReader = new MemberReader(memberRepository);
    }

    @Test
    @DisplayName("회원 이름 비밀번호로 회원을 찾아온다.")
    void readLoginMember() {
        // given
        String username = "username";
        String password = "password";
        Member member = MemberFixture.createMember(username, password);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));

        // when
        Member readMember = memberReader.login(username, password);

        // then
        assertThat(readMember).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 이름과 비밀번호로 회원을 찾아올 때, 회원 이름이 틀리면 예외가 발생한다.")
    void readLoginMemberByInvalidUsername() {
        // given
        String username = "username";
        String password = "password";
        Member member = MemberFixture.createMember(username, password);
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberReader.login(username + "anyString", password))
                .isInstanceOf(BadRequestException.class)
                .extracting((exception) -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_LOGIN);
    }

    @Test
    @DisplayName("회원 이름과 비밀번호로 회원을 찾아올 때, 비밀번호가 틀리면 예외가 발생한다.")
    void readLoginMemberByInvalidPassword() {
        // given
        String username = "username";
        String password = "password";
        Member member = MemberFixture.createMember(username, password);
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberReader.login(username, password + "anyString"))
                .isInstanceOf(BadRequestException.class)
                .extracting((exception) -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_LOGIN);
    }
}
