package com.lof.global;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// TODO
//  1. prod와 local 분리 시, RedisInitializer는 local에서만 실행되도록 profile 설정
//  2. 테스트 시 redis 어떻게 할지 고민
@Component
@Profile("local")
@RequiredArgsConstructor
public class RedisInitializer {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
