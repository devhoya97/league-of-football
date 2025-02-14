package com.lof.member.service;

import org.springframework.stereotype.Service;

import com.lof.member.domain.Member;
import com.lof.member.implement.MemberWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberWriter memberWriter;

    public void signUp(Member member) {
        memberWriter.save(member);
    }
}
