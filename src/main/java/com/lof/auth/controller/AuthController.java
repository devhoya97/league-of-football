package com.lof.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lof.auth.controller.dto.LoginRequest;
import com.lof.auth.controller.dto.LoginResponse;
import com.lof.auth.implement.dto.LoginToken;
import com.lof.auth.service.AuthService;
import com.lof.global.exception.BizException;
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

    /*
    TODO
        2. bearer를 지금 안 붙이고 사용중인 것 같은데 수정하기
     */
    @PostMapping("/login-refresh")
    public LoginResponse loginRefresh(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);
        LoginToken token = authService.reissueLoginToken(refreshToken);
        return new LoginResponse(token);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);
        authService.logout(refreshToken);
    }

    // refreshToken을 다루는 로직이 여기 둘 밖에 없어서, 인터셉터에서는 accessToken만 다루도록 했음
    private String getRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (refreshToken == null) {
            throw new BizException(ErrorCode.MISSING_TOKEN);
        }
        return refreshToken;
    }
}
