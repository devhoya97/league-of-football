package com.lof.auth.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lof.auth.controller.dto.LoginRequest;
import com.lof.auth.controller.dto.LoginResponse;
import com.lof.auth.domain.LoginToken;
import com.lof.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {

        /*
        1. 웹계층에서 사용하는 dto를 service 계층까지 의존하지 않기 위해 값을 까서 넘김.
        만약 파라미터가 너무 많다면, service 호출을 위한 새로운 DTO를 만들 것 같다.
        2. 유효한 member 엔티티를 생성해주는 객체(MemberReader)를 controller에서 호출하고, authService에게는
        유효한 member 엔티티를 넘겨주는 방식이 더 좋을까 고민해봤지만, MemberReader가 DB단에 접근해야 한다는 점에서
        service의 앞단으로 두기엔 service 계층을 건너뛰게 되어 좋지 않다고 생각했음. 그렇다고 service 계층에 포함시키기엔
        비즈니스 로직을 수행한다는 기존 service 계층의 성격과 MemberReader의 역할이 잘 어울리지 않음.
        따라서 service가 비즈니스 로직을 수행하기 위해 필요한 검증 따위의 작업을 도구(implement) 계층이라는 하위 계층에 위임하고
         service는 도구 계층의 객체들을 활용하는 방식으로 구현
         */
        LoginToken token = authService.createLoginToken(loginRequest.loginId(), loginRequest.password());
        return new LoginResponse(token);
    }
}
