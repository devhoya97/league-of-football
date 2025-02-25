package com.lof.auth.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lof.auth.controller.dto.LoginRequest;
import com.lof.auth.controller.dto.LoginResponse;
import com.lof.auth.implement.dto.LoginToken;
import com.lof.auth.service.AuthService;
import com.lof.member.controller.SignUpRequest;
import com.lof.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpRequest request) {
        Member member = new Member(request.username(), request.password());

        authService.signUp(member);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginToken token = authService.issueLoginToken(loginRequest.username(), loginRequest.password());
        return new LoginResponse(token);
    }

    /*
    TODO
        1. refreshToken은 Authorization 헤더로 받지 않고 refresh라는 커스텀 헤더로 받는 것 같은데 더 알아보기
        2. bearer를 지금 안 붙이고 사용중인 것 같은데 수정하기
        3. 인가 요청시 accessToken이 아닌 refreshToken을 사용하는 경우, 인가에 실패하도록 구현하기
     */
    @PostMapping("/login-refresh")
    public LoginResponse loginRefresh(@RequestAttribute long memberId,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        LoginToken token = authService.reissueLoginToken(memberId, refreshToken);
        return new LoginResponse(token);
    }

    @PostMapping("/logout")
    public void logout(@RequestAttribute long memberId,
                       @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        authService.logout(memberId, refreshToken);
    }
}
