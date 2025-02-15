package com.lof.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lof.auth.controller.dto.LoginRequest;
import com.lof.auth.controller.dto.LoginResponse;
import com.lof.auth.service.dto.LoginToken;
import com.lof.auth.service.AuthService;
import com.lof.global.exception.AuthException;
import com.lof.global.exception.ErrorCode;
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

    @GetMapping("/login-refresh")
    public LoginResponse loginRefresh(HttpServletRequest request) {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (refreshToken == null) {
            throw new AuthException(ErrorCode.MISSING_TOKEN);
        }
        LoginToken token = authService.reissueLoginToken(refreshToken);
        return new LoginResponse(token);
    }

    // TODO: 로그아웃 구현
}
