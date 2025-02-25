package com.lof.waitingqueue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lof.member.domain.Member;
import com.lof.member.implement.MemberDao;
import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.implement.WaitingQueueSelector;
import com.lof.waitingqueue.implement.WaitingQueueValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final MemberDao memberDao;
    private final WaitingQueueValidator queueValidator;
    private final WaitingQueueSelector queueSelector;

    @Transactional // waitingQueue의 status를 COMPLETED로 바꾸는 작업과 member 테이블의 queue_id 값을 바꾸는 작업은 원자성을 가져야 하므로
    public long join(long memberId) {
        //TODO: member 가져오는 로직은 이제 너무 중복될 예정이니까, ArgumentResolver로 빼자.
        Member member = memberDao.getMemberById(memberId);
        queueValidator.validateMember(member);
        WaitingQueue queue = queueSelector.selectQueue(member.getRankScore());
        queue.addMember(member);

        return queue.getId();
    }
}
