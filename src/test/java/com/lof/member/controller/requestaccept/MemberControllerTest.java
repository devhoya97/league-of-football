package com.lof.member.controller.requestaccept;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.lof.member.controller.MemberController;
import com.lof.member.fixture.MemberFixture;
import com.lof.member.service.MemberService;
import com.lof.member.service.MemberValidator;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;
    @MockitoBean
    private MemberValidator memberValidator;

    @Test
    @DisplayName("명세에 맞게 회원 가입 API를 호출하면, 201 응답코드를 반환한다.")
    void signUp() throws Exception {
        // given
        String signUpRequest = MemberFixture.createSignUpRequestString(MemberFixture.VALID_LOGIN_ID, MemberFixture.VALID_PASSWORD);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @DisplayName("회원 아이디는 빈 문자열이나 공백으로만 구성되어 있을 수 없다.")
    @ValueSource(strings = {"", " "})
    void signUpFailByLoginId(String loginId) throws Exception {
        // given
        String signUpRequest = MemberFixture.createSignUpRequestString(loginId, MemberFixture.VALID_PASSWORD);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("회원 비밀번호는 빈 문자열이나 공백으로만 구성되어 있을 수 없다.")
    @ValueSource(strings = {"", " "})
    void signUpFailByPassword(String password) throws Exception {
        // given
        String signUpRequest = MemberFixture.createSignUpRequestString(MemberFixture.VALID_LOGIN_ID, password);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest());
    }
}
