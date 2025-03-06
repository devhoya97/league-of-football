package com.lof.game;

import com.lof.game.domain.Game;
import com.lof.waitingqueue.domain.WaitingQueue;

public class GameFixture {

    private GameFixture() {
    }

    public static Game gameWithId(long id, WaitingQueue queue) {
        Game game = new Game(queue);
        game.setId(id);

        return game;
    }
}
