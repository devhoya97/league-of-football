package com.lof.member.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.lof.member.fixture.MemberFixture;
import com.lof.member.repository.MemberRepository;

// TODO: WebMvcTest에서는 HTTP 요청을 request DTO로 변환하는 과정을 테스트하고
//  여기서는 그 뒷단을 테스트하게 되면 RestAssured를 활용한 통합테스트가 필요없는게 아닐까?
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class MemberControllerTest {

    @Autowired
    private MemberController memberController;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입을 위한 검증에 통과하면, 회원가입에 성공한다.")
    void signUp() {
        // given
        SignUpRequest signUpRequest = MemberFixture.createSignUpRequest(MemberFixture.VALID_LOGIN_ID, MemberFixture.VALID_PASSWORD);
        int initialSize = memberRepository.findAll().size();

        // when
        memberController.signUp(signUpRequest);

        // then
        assertThat(memberRepository.findAll().size()).isEqualTo(initialSize + 1);
    }
}
