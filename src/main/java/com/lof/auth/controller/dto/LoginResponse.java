package com.lof.auth.controller.dto;

import com.lof.auth.domain.LoginToken;

public record LoginResponse(String accessToken, String refreshToken) {

    public LoginResponse(LoginToken loginToken) {
        this(loginToken.accessToken(), loginToken.refreshToken());
    }
}
