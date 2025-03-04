package com.lof.waitingqueue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lof.member.domain.Member;
import com.lof.member.implement.MemberDao;
import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.implement.WaitingQueueManager;
import com.lof.waitingqueue.implement.WaitingQueueValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final MemberDao memberDao;
    private final WaitingQueueManager waitingQueueManager; // TODO: 클래스 분리 고려
    private final WaitingQueueValidator waitingQueueValidator;

    @Transactional
    public long join(long memberId) {
        waitingQueueValidator.validateNotInMatchingQueue(memberId);
        Member member = memberDao.getMemberById(memberId);
        WaitingQueue waitingQueue = waitingQueueManager.selectQueue(member.getRankScore());
        waitingQueueManager.addMember(member, waitingQueue);

        return waitingQueue.getId();
    }

    @Transactional
    public void leave(long memberId) {
        waitingQueueValidator.validateInMatchingQueue(memberId);
        waitingQueueManager.leaveMatchingQueue(memberId);
    }
}
