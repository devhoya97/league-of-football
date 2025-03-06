package com.lof.game.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lof.game.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByWaitingQueueId(long waitingQueueId);
}
