package com.lof.member.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void signUp(Member member) {
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            // loginId 외에 unique 제약조건이 걸린 컬럼이 추가된다면, 예외 메시지 파싱하여 분기처리 필요
            throw new BadRequestException(ErrorCode.DUPLICATED_LOGINID, e);
        }
    }
}
