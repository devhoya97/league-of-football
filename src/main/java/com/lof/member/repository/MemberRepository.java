package com.lof.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lof.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // TODO: 테스트 작성
    Optional<Member> findByLoginId(String loginId);
}
