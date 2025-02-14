package com.lof.member.implement;

import org.springframework.dao.DataIntegrityViolationException;
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

    /*
    여러 스레드가 동시에 validateDuplicatedLoginId 로직을 통과하면 loginId의 unique 제약조건에 의해
     DataIntegrityViolationException이 발생할텐데, 자주 발생하는 일은 아니므로 이를 그냥 500으로 처리할지
     예외 메시지를 구체적으로 내려줘야 할지 고민했음. 자주 발생할만한 일은 아니지만 이 경우에는 적절한 예외메시지를
     내려줘야 사용자가 loginId를 바꿔서 재시도하도록 유도할 수 있으므로 loginId의 unique 조건에 의해 발생한
     경우만 특수하게 처리했음
     */
    public void save(Member member) {
        validateDuplicatedLoginId(member);
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throwCustomExceptionIfDuplicatedLoginId(member, e);
        }
    }

    private void validateDuplicatedLoginId(Member member) {
        if (memberRepository.existsByLoginId(member.getLoginId())) {
            throw new BadRequestException(ErrorCode.DUPLICATED_LOGINID);
        }
    }

    private void throwCustomExceptionIfDuplicatedLoginId(Member member, DataIntegrityViolationException e) {
        if (isLoginIdUniqueViolation(e, member.getLoginId())) {
            throw new BadRequestException(ErrorCode.DUPLICATED_LOGINID, e);
        }
        throw e;
    }

    private boolean isLoginIdUniqueViolation(DataIntegrityViolationException e, String loginId) {
        String message = e.getMessage();
        if (message == null) {
            return false;
        }
        String lowerCaseMessage = message.toLowerCase();
        return lowerCaseMessage.contains("unique") && (lowerCaseMessage.contains("loginid") || lowerCaseMessage.contains(loginId));
    }
}
