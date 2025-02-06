package com.lof.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import com.lof.member.domain.Member;
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
    @DisplayName("회원 가입을 위한 검증에 통과하면, 회원가입에 성공한다.")
    void signUp() throws Exception {
        // given
        String signUpRequest = """
            {
                "loginId": "abc123",
                "password": "1234"
            }
            """;
        doNothing().when(memberValidator).validate(any(Member.class));
        doNothing().when(memberService).signUp(any(Member.class));

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isCreated());
        verify(memberValidator, times(1)).validate(any(Member.class));
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
