package com.lof.waitingqueue.implement;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingQueueValidator {

    private final WaitingQueueManager queueDao;

    public void validateNotInMatchingQueue(long memberId) {
        if (queueDao.isInMatchingQueue(memberId)) {
            throw new BizException(ErrorCode.ALREADY_WAITING);
        }
    }

    public void validateInMatchingQueue(long memberId) {
        if (!queueDao.isInMatchingQueue(memberId)) {
            throw new BizException(ErrorCode.NOT_IN_WAITING);
        }
    }

//    public void validateMember(Member member) {
//        // getter로 직접 꺼내서 비교하는게 낫나?
//        if (member.isAlreadyInWaitingQueue()) {
//            throw new BizException(ErrorCode.ALREADY_WAITING);
//        }
//    }

//    public void
}
