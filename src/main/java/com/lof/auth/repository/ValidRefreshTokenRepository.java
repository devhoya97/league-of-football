package com.lof.auth.repository;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.lof.member.domain.Member;

import lombok.RequiredArgsConstructor;

// TODO: @Repository를 하면 DB 접근 시 예외를 스프링 예외로 변환해주는데, Redis 접근시 발생하는 예외도 포함되는지 확인
@Repository
@RequiredArgsConstructor
public class ValidRefreshTokenRepository {

    private static final String VALID_REFRESH_TOKEN = "valid-refresh-token:";
    private static final String INVALID_REFRESH_TOKEN = "invalid-refresh-token:";

    private final RedisTemplate<String, String> redisTemplate;

    public Optional<String> findByMemberId(long memberId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(VALID_REFRESH_TOKEN + memberId));
    }

    public void save(Member member, String refreshToken) {
        redisTemplate.opsForValue().set(VALID_REFRESH_TOKEN + member.getId(), refreshToken);
    }

    public void deleteByMemberId(long memberId) {
        redisTemplate.delete(VALID_REFRESH_TOKEN + memberId);
    }
}
