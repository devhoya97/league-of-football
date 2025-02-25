package com.lof.waitingqueue.implement;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;

@Component
public class WaitingQueueValidator {

    public void validateMember(Member member) {
        // getter로 직접 꺼내서 비교하는게 낫나?
        if (member.isAlreadyInWaitingQueue()) {
            throw new BizException(ErrorCode.ALREADY_WAITING);
        }
    }
}
