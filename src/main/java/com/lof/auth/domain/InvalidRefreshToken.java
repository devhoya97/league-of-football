package com.lof.auth.domain;

import jakarta.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash("InvalidRefreshToken")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InvalidRefreshToken {

    @Id
    private String id; // 변수명을 refreshToken으로 못 바꾸나?
//    private String refreshToken;
    private long memberId;

    @TimeToLive
    private Integer expirationInSeconds;

    public InvalidRefreshToken(ValidRefreshToken validRefreshToken, int expirationInSeconds) {
        this(validRefreshToken.getRefreshToken(), validRefreshToken.getId(), expirationInSeconds);
    }
}
