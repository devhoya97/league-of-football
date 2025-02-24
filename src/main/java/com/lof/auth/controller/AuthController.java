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
