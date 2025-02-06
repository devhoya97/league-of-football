package com.lof.member.controller;

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

import com.lof.member.service.MemberService;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입에 성공하면 201 상태코드를 반환한다.")
    void signUp() throws Exception {
        // given
        String signUpRequest = """
            {
                "loginId": "abc123",
                "password": "1234"
            }
            """;

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @DisplayName("회원 아이디는 빈 문자열이나 공백으로만 구성되어 있을 수 없다.")
    @ValueSource(strings = {
            """
            {
                "loginId": "",
                "password": "1234"
            }
            """,
            """
            {
                "loginId": " ",
                "password": "1234"
            }
            """,
            """
            {
                "password": "1234"
            }
            """
    })
    void signUpFailByLoginId(String signUpRequest) throws Exception {
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("회원 비밀번호는 빈 문자열이나 공백으로만 구성되어 있을 수 없다.")
    @ValueSource(strings = {
            """
            {
                "loginId": "abc123",
                "password": ""
            }
            """,
            """
            {
                "loginId": "abc123",
                "password": " "
            }
            """,
            """
            {
                "loginId": "abc123"
            }
            """
    })
    void signUpFailByPassword(String signUpRequest) throws Exception {
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest());
    }
}
