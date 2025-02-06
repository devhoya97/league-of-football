package com.lof.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("loginId가 중복된 회원 생성을 시도하면 예외가 발생한다.")
    void createMember() {
        // given
        Member member1 = Member.builder().loginId("sameId").password("1111").build();
        Member member2 = Member.builder().loginId("sameId").password("2222").build();
        memberService.createMember(member1);

        // when & then
        assertThatThrownBy(() -> memberService.createMember(member2))
                .isInstanceOf(BadRequestException.class)
                .extracting(exception -> ((BadRequestException) exception).getCode())
                .isEqualTo(ErrorCode.DUPLICATED_LOGINID);
    }

}
