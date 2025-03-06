package com.lof.game.implement;

import org.springframework.stereotype.Component;

import com.lof.game.domain.Game;
import com.lof.game.repository.GameRepository;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.waitingqueue.domain.WaitingQueue;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GameManager {

    private final GameRepository gameRepository;

    public Game createGame(WaitingQueue waitingQueue) {
        waitingQueue.decideTeam();
        Game game = new Game(waitingQueue);
        return gameRepository.save(game);
    }

    public Game readByQueueId(long queueId) {
        return gameRepository.findByWaitingQueueId(queueId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_GAME_INTERNAL_SERVER_ERROR));
    }
}
