package com.lof.member.implement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

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
    @DisplayName("loginId가 중복되어 DataIntegrityViolationException이 발생하면 BadRequestException으로 전환시킨다.")
    void saveDuplicatedLoginId() {
        // given
        Member member = MemberFixture.createMember("loginId", "password");
        when(memberRepository.save(member)).thenThrow(new DataIntegrityViolationException("Duplicated loginId"));

        // when & then
        assertThatThrownBy(() -> memberWriter.save(member))
                .isInstanceOf(BadRequestException.class)
                .extracting(exception -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.DUPLICATED_LOGINID);
    }

    @Test
    @DisplayName("loginId가 중복되는 경우를 제외하고 DataIntegrityViolationException이 발생하면 예외를 전환시키지 않는다.")
    void saveThrowDataIntegrityViolationException() {
        // given
        Member member = MemberFixture.createMember("loginId", "password");
        when(memberRepository.save(member)).thenThrow(new DataIntegrityViolationException("fk constraints"));

        // when & then
        assertThatThrownBy(() -> memberWriter.save(member))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
