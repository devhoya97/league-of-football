package com.lof.auth.controller.dto;

import com.lof.auth.service.dto.LoginToken;

public record LoginResponse(String accessToken, String refreshToken) {

    public LoginResponse(LoginToken loginToken) {
        this(loginToken.accessToken(), loginToken.refreshToken());
    }
}
