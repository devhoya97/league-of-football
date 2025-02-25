package com.lof.waitingqueue.implement;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.repository.WaitingQueueRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingQueueSelector {

    private static final int SCORE_SELECT_BOUND = 50;

    private final WaitingQueueRepository queueRepository;

    public WaitingQueue selectQueue(int rankScore) {
        Optional<WaitingQueue> found = queueRepository.findByAvgScoreBetween(rankScore - SCORE_SELECT_BOUND, rankScore + SCORE_SELECT_BOUND);
        if (found.isPresent()) {
            return found.get();
        }
        WaitingQueue queue = new WaitingQueue();
        queueRepository.save(queue);
        return queue;
    }
}
