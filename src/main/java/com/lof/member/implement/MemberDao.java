package com.lof.member.implement;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/**
 * baeldung의 설명을 보면, Dao가 DB에 더 가깝고 Repository가 Dao를 이용한다고 되어있는데,
 * 네이밍이 괜찮을지?
 * https://www.baeldung.com/java-dao-vs-repository
 */
@Component
@RequiredArgsConstructor
public class MemberDao {

    private final MemberRepository memberRepository;

    public void save(Member member) {
        validateDuplicatedUsername(member);
        memberRepository.save(member);
    }

    private void validateDuplicatedUsername(Member member) {
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new BadRequestException(ErrorCode.DUPLICATED_USERNAME);
        }
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_LOGIN));
    }

    public Member getMemberById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId = " + memberId));
    }
}
