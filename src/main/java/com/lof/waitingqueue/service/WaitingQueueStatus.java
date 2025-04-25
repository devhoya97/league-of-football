package com.lof.waitingqueue.service;

public record WaitingQueueStatus(
        int queueSize,
        long gameId
) {
}
