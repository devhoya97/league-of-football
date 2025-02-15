package com.lof.member.implement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.fixture.MemberFixture;
import com.lof.member.repository.MemberRepository;

class MemberWriterTest {

    private MemberWriter memberWriter;

    private MemberRepository memberRepository;

    @BeforeEach
    void init() {
        memberRepository = mock(MemberRepository.class);
        memberWriter = new MemberWriter(memberRepository);
    }

    @Test
    @DisplayName("이미 존재하는 회원 이름으로 회원 가입에 시도하면, 지정해둔 예외가 발생한다.")
    void saveDuplicatedUsername() {
        // given
        String username = "existUsername";
        when(memberRepository.existsByUsername(username)).thenReturn(true);
        Member member = MemberFixture.createMember(username, "password");

        // when & then
        assertThatThrownBy(() -> memberWriter.save(member))
                .isInstanceOf(BadRequestException.class)
                .extracting(e -> ((BadRequestException) e).getCode())
                .isEqualTo(ErrorCode.DUPLICATED_USERNAME);
    }
}
