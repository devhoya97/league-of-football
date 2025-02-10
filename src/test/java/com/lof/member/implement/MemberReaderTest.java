package com.lof.member.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.fixture.MemberFixture;
import com.lof.member.repository.MemberRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
class MemberReaderTest {

    @Autowired
    private MemberReader memberReader;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 아이디와 비밀번호로 회원을 찾아온다.")
    void readLoginMember() {
        // given
        String loginId = "testLoginId";
        String password = "testPassword";
        Member member = MemberFixture.createMember(loginId, password);
        memberRepository.save(member);

        // when
        Member readMember = memberReader.readLoginMember(loginId, password);

        // then
        assertThat(readMember).isEqualTo(member);
    }

    @Test
    @DisplayName("로그인 아이디와 비밀번호로 회원을 찾아올 때, 아이디가 틀리면 예외가 발생한다.")
    void readLoginMemberByInvalidLoginId() {
        // given
        String loginId = "testLoginId";
        String password = "testPassword";
        Member member = MemberFixture.createMember(loginId, password);
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberReader.readLoginMember(loginId + "anyString", password))
                .isInstanceOf(BadRequestException.class)
                .extracting((exception) -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_LOGIN);
    }

    @Test
    @DisplayName("로그인 아이디와 비밀번호로 회원을 찾아올 때, 비밀번호가 틀리면 예외가 발생한다.")
    void readLoginMemberByInvalidPassword() {
        // given
        String loginId = "testLoginId";
        String password = "testPassword";
        Member member = MemberFixture.createMember(loginId, password);
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberReader.readLoginMember(loginId, password + "anyString"))
                .isInstanceOf(BadRequestException.class)
                .extracting((exception) -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.INVALID_LOGIN);
    }
}
