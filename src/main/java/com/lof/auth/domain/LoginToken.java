package com.lof.auth.domain;

public record LoginToken(String accessToken, String refreshToken) {
}
