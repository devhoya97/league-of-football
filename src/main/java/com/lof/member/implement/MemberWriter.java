package com.lof.member.implement;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberWriter {

    private final MemberRepository memberRepository;

    /**
     * validateDuplicatedUsername이 만약 없다면, unique 제약조건에 의해 예외가 터질텐데
     * validateDuplicatedUsername에서 먼저 확인했을 때 어떤 이점이 있는가?
     */
    public void save(Member member) {
        validateDuplicatedUsername(member);
        memberRepository.save(member);
    }

    private void validateDuplicatedUsername(Member member) {
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new BadRequestException(ErrorCode.DUPLICATED_USERNAME);
        }
    }
}
