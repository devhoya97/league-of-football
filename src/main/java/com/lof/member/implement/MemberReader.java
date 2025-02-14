package com.lof.member.implement;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberReader {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_LOGIN));

        // Member 엔티티 안에 isCorrectPassword() 메서드를 만드는게 나을까? 아니면 여기서 getter로 비교하는게 나을까?
        // 객체지향적으로 설계한다면, 객체에게 직접 묻는 방법이 나을 것 같은데, 이렇게 간단한 로직을 굳이 메서드로 빼야하나 싶기도 함.
        if (!Objects.equals(member.getPassword(), password)) {
            throw new BadRequestException(ErrorCode.INVALID_LOGIN);
        }

        return member;
    }

    public Member read(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId = " + memberId));
    }
}
