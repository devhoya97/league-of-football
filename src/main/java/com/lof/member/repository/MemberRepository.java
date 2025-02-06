package com.lof.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lof.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
