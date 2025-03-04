package com.lof.waitingqueue.implement;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.lof.member.domain.Member;
import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.domain.WaitingQueueMember;
import com.lof.waitingqueue.repository.WaitingQueueMemberRepository;
import com.lof.waitingqueue.repository.WaitingQueueRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingQueueManager {

    private static final int SCORE_SELECT_BOUND = 50;

    private final WaitingQueueRepository queueRepository;
    private final WaitingQueueMemberRepository queueMemberRepository;

    public WaitingQueue selectQueue(int rankScore) {
        List<WaitingQueue> waitingQueueList = queueRepository.findByAvgScoreBetween(rankScore - SCORE_SELECT_BOUND, rankScore + SCORE_SELECT_BOUND, PageRequest.of(0, 1));
        if (!waitingQueueList.isEmpty()) {
            return waitingQueueList.get(0);
        }
        WaitingQueue queue = new WaitingQueue();
        queueRepository.save(queue);
        return queue;
    }

    public void addMember(Member member, WaitingQueue queue) {
        WaitingQueueMember queueMember = new WaitingQueueMember(member, queue);
        queueMemberRepository.save(queueMember);
        queue.addWaitingQueueMember(queueMember);
    }

    public boolean isInMatchingQueue(long memberId) {
        return queueMemberRepository.isInMatchingQueue(memberId);
    }

    public void leaveMatchingQueue(long memberId) {
        WaitingQueueMember queueMember = queueMemberRepository.findWaitingMember(memberId);
        WaitingQueue waitingQueue = queueMember.getWaitingQueue();
        if (waitingQueue.hasOnlyOneWaitingMember()) {
            waitingQueue.cancelMatching();
        }

        queueMember.leaveWaitingQueue();
    }
}
