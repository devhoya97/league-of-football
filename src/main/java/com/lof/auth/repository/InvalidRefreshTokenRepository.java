package com.lof.auth.repository;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

// TODO: @Repository를 하면 DB 접근 시 예외를 스프링 예외로 변환해주는데, Redis 접근시 발생하는 예외도 포함되는지 확인
@Repository
@RequiredArgsConstructor
public class InvalidRefreshTokenRepository {

    private final RedisTemplate<String, Long> redisLongTemplate;

    public Optional<Long> findByRefreshToken(String refreshToken) {
        return Optional.ofNullable(redisLongTemplate.opsForValue().get(refreshToken));
    }

    // refreshToken 값이 겹칠 경우의 수도 고려해야하나? 똑같은 키로 넣으려고 할테니 redis단에서 에러나겠구나.
    public void save(long memberId, String refreshToken) {
        redisLongTemplate.opsForValue().set(refreshToken, memberId);
    }
}
