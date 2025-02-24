package com.lof.auth.domain;

import jakarta.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash(value = "ValidRefreshToken", timeToLive = 1209600L) // TODO: TTL을 환경변수 사용하도록 변경
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ValidRefreshToken {

    @Id
    private Long id; // 변수명을 memberId로 못 바꾸나?
    //    private Long memberId;
    private String refreshToken;
}
