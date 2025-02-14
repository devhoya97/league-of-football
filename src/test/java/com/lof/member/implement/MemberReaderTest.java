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
    @DisplayName("로그인 아이디와 비밀번호로 회원을 찾아온다.")
    void readLoginMember() {
        // given
        String loginId = "loginId";
        String password = "password";
        Member member = MemberFixture.createMember(loginId, password);
        when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.of(member));

        // when
        Member readMember = memberReader.login(loginId, password);

        // then
        assertThat(readMember).isEqualTo(member);
    }

    @Test
    @DisplayName("로그인 아이디와 비밀번호로 회원을 찾아올 때, 아이디가 틀리면 예외가 발생한다.")
    void readLoginMemberByInvalidLoginId() {
        // given
        String loginId = "loginId";
        String password = "password";
        Member member = MemberFixture.createMember(loginId, password);
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberReader.login(loginId + "anyString", password))
                .isInstanceOf(BadRequestException.class)
                .extracting((exception) -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_LOGIN);
    }

    @Test
    @DisplayName("로그인 아이디와 비밀번호로 회원을 찾아올 때, 비밀번호가 틀리면 예외가 발생한다.")
    void readLoginMemberByInvalidPassword() {
        // given
        String loginId = "loginId";
        String password = "password";
        Member member = MemberFixture.createMember(loginId, password);
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberReader.login(loginId, password + "anyString"))
                .isInstanceOf(BadRequestException.class)
                .extracting((exception) -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_LOGIN);
    }
}
