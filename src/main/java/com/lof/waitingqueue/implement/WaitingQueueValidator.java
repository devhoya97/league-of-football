package com.lof.waitingqueue.implement;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.waitingqueue.repository.WaitingQueueMemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingQueueValidator {

    private final WaitingQueueMemberRepository waitingQueueMemberRepository;

    public void validateNotInMatchingQueue(long memberId, LocalDate gameDate) {
        if (waitingQueueMemberRepository.isJoinedInMatchingQueue(memberId, gameDate)) {
            throw new BizException(ErrorCode.ALREADY_WAITING);
        }
    }
}
