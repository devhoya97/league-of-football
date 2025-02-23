package com.lof.auth.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.lof.auth.implement.dto.LoginToken;
import com.lof.global.ControllerTest;
import com.lof.global.exception.ErrorCode;
import com.lof.member.fixture.MemberFixture;

class AuthControllerTest extends ControllerTest {

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
    @DisplayName("회원 이름 규칙에 벗어나면 예외가 발생한다.")
    @ValueSource(strings = {"aaaa", "12345", "a123456789a1234567890"})
    void signUpFailByUsername(String username) throws Exception {
        // given
        String signUpRequest = MemberFixture.createSignUpRequestString(username, MemberFixture.VALID_PASSWORD);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code", is(ErrorCode.INVALID_USER_INPUT.toString())))
                .andExpect(jsonPath("message", is("회원 이름은 5~20자의 영문과 숫자로 구성되어야 하며, 숫자로만 이루어질 수 없습니다.")));
    }

    @ParameterizedTest
    @DisplayName("회원 비밀번호 규칙에 벗어나면 예외가 발생한다.")
    @ValueSource(strings = {"12345678", "a1234567", "Aa123456", "Aa1234!", "A123456789a123456789!"})
    void signUpFailByPassword(String password) throws Exception {
        // given
        String signUpRequest = MemberFixture.createSignUpRequestString(MemberFixture.VALID_LOGIN_ID, password);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code", is(ErrorCode.INVALID_USER_INPUT.toString())))
                .andExpect(jsonPath("message", is("비밀번호는 8~20자이며, 대문자, 소문자, 숫자, 특수문자를 최소 하나씩 포함해야 합니다.")));
    }

    @Test
    @DisplayName("회원이름과 비밀번호로 로그인을 하면 토큰이 발급된다.")
    void login() throws Exception {
        // given
        String loginRequest = MemberFixture.createLoginRequestString(MemberFixture.VALID_LOGIN_ID, MemberFixture.VALID_PASSWORD);
        when(authService.issueLoginToken(MemberFixture.VALID_LOGIN_ID, MemberFixture.VALID_PASSWORD))
                .thenReturn(new LoginToken("accessToken", "refreshToken"));

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accessToken", is("accessToken")))
                .andExpect(jsonPath("refreshToken", is("refreshToken")));
    }

    @Test
    @DisplayName("refreshToken으로 accessToken과 refreshToken을 갱신한다.")
    void loginRefresh() throws Exception {
        // given
        when(authService.reissueLoginToken(1L, "refreshToken"))
                .thenReturn(new LoginToken("accessToken", "newRefreshToken"));
        when(tokenParser.parseMemberId("refreshToken"))
                .thenReturn(1L);

        // when & then
        mockMvc.perform(get("/login-refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "refreshToken"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accessToken", is("accessToken")))
                .andExpect(jsonPath("refreshToken", is("newRefreshToken")));
    }
}
