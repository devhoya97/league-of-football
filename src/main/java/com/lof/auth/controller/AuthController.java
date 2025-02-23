package com.lof.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
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

    /**
     * refreshToken으로부터 memberId를 파싱하는 경우는 이 때 밖에 없어서, 굳이 필터나 인터셉터로 빼지 않아도 될 것 같다.
     * 다른 API 구현할 때도, memberId나 Member 객체가 필요하다면 필터, 인터셉터보단 argumentResolver를 활용하는게 낫지 않을까?
     * HttpServletRequest.setAttribute()로 값을 넣어주는 것보다 argumentResolver를 사용하는게 더 직관적인 것 같음.
     * 필터, 인터셉터는 딱 로깅이랑 어드민이 맞는 것 같음
     */
    @PostMapping("/login-refresh")
    public LoginResponse loginRefresh(HttpServletRequest request) {
        long memberId = (long) request.getAttribute("memberId");
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        LoginToken token = authService.reissueLoginToken(memberId, refreshToken);
        return new LoginResponse(token);
    }

    @PostMapping("/logout")
    public void logout(@RequestAttribute long memberId,
                       @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        authService.logout(memberId, refreshToken);
    }
}
