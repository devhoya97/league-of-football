package com.lof.member.implement;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberManager {

    private final MemberRepository memberRepository;

    public long save(Member member) {
        Member saved = memberRepository.save(member);

        return saved.getId();
    }

    public List<Member> getMembers(Set<Long> memberIds) {
        List<Member> members = memberRepository.findAllById(memberIds);
        if (members.size() != memberIds.size()) {
            throw new BizException(ErrorCode.NOT_FOUND_MEMBER);
        }
        return members;
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new BizException(ErrorCode.INVALID_LOGIN));
    }

    public Member getMemberById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
