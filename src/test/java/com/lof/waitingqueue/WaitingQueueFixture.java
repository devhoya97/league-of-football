package com.lof.waitingqueue;

import java.time.LocalDate;

import com.lof.game.GameFixture;
import com.lof.game.domain.Game;
import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.service.JoinResult;

public class WaitingQueueFixture {

    private WaitingQueueFixture() {
    }

    public static WaitingQueue waitingQueueWithId(long id, LocalDate gameDate) {
        WaitingQueue queue = new WaitingQueue(gameDate);
        queue.setId(id);
        return queue;
    }

    public static JoinResult joinResult() {
        WaitingQueue queue = waitingQueueWithId(1L, LocalDate.now());
        Game game = GameFixture.gameWithId(1L, queue);

        return new JoinResult(queue, game);
    }
}
