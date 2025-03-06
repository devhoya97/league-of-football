package com.lof.waitingqueue.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lof.game.domain.Game;
import com.lof.game.implement.GameManager;
import com.lof.member.domain.Member;
import com.lof.member.implement.MemberManager;
import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.implement.WaitingQueueManager;
import com.lof.waitingqueue.implement.WaitingQueueValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final MemberManager memberManager;
    private final WaitingQueueManager waitingQueueManager;
    private final WaitingQueueValidator waitingQueueValidator;
    private final GameManager gameManager;

    @Transactional
    public JoinResult join(long memberId, LocalDate gameDate) {
        waitingQueueValidator.validateNotInMatchingQueue(memberId, gameDate);
        Member member = memberManager.getMemberById(memberId);
        WaitingQueue waitingQueue = waitingQueueManager.selectQueue(member.getRankScore(), gameDate);
        waitingQueue.joinMember(member);
        if (waitingQueue.isCompleted()) {
            Game game = gameManager.createGame(waitingQueue);
            return new JoinResult(waitingQueue, game);
        }
        return new JoinResult(waitingQueue, null);
    }

    @Transactional
    public void leave(long memberId, long queueId) {
        WaitingQueue waitingQueue = waitingQueueManager.readById(queueId);
        waitingQueue.leave(memberId);
    }

    public JoinResult checkMatchingStatus(long queueId) {
        WaitingQueue waitingQueue = waitingQueueManager.readById(queueId);
        if (waitingQueue.isCompleted()) {
            Game game = gameManager.readByQueueId(waitingQueue.getId());
            return new JoinResult(waitingQueue, game);
        }
        return new JoinResult(waitingQueue, null);
    }

    //TODO: 하루가 바뀔 때마다 MATCHING 상태인 대기열 중 날짜가 지난 대기열들의 상태를 CANCEL로 바꾸고, WaitingQueueMember의 상태도 LEFT로 바꾸기
}
