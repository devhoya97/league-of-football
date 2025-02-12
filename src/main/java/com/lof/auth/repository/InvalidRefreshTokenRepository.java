package com.lof.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lof.auth.domain.InvalidRefreshToken;

// TODO: @Repository를 하면 DB 접근 시 예외를 스프링 예외로 변환해주는데, Redis 접근시 발생하는 예외도 포함되는지 확인
@Repository
public interface InvalidRefreshTokenRepository extends CrudRepository<InvalidRefreshToken, String> {
}
