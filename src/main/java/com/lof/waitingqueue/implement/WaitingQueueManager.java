package com.lof.waitingqueue.implement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.repository.WaitingQueueRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingQueueManager {

    private static final int SCORE_SELECT_BOUND = 50;

    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueue readById(long queueId) {
        return waitingQueueRepository.findById(queueId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_EXIST_WAITING_QUEUE));
    }

    public WaitingQueue selectQueue(int rankScore, LocalDate gameDate) {
        List<WaitingQueue> selectResult = waitingQueueRepository.findWaitingQueueAvgScoreInRangeOrderByCreatedAt(
                rankScore - SCORE_SELECT_BOUND,
                rankScore + SCORE_SELECT_BOUND,
                gameDate,
                PageRequest.of(0, 1));

        if (!selectResult.isEmpty()) {
            return selectResult.get(0);
        }
        WaitingQueue waitingQueue = new WaitingQueue(gameDate);
        waitingQueueRepository.save(waitingQueue);
        return waitingQueue;
    }
}
