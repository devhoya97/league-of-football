package com.lof.waitingqueue.controller.dto;

import com.lof.waitingqueue.service.WaitingQueueStatus;

public record JoinResponse(
        int queueSize,
        long gameId
) {
    public JoinResponse(WaitingQueueStatus waitingQueueStatus) {
        this(waitingQueueStatus.queueSize(), waitingQueueStatus.gameId());
    }
}
