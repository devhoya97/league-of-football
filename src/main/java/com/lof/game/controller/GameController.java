package com.lof.game.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lof.game.service.GameInfo;
import com.lof.game.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/{gameId}")
    public GameResponse findGame(@PathVariable long gameId) {
        GameInfo gameInfo = gameService.findGameInfo(gameId);

        return new GameResponse(gameInfo);
    }

    @PostMapping("/{gameId}/vote")
    public void submitWinVote(@RequestBody WinVoteRequest winVoteRequest,
                              @PathVariable long gameId,
                              @RequestAttribute long memberId) {
        gameService.submitWinVote(winVoteRequest.winVote(), gameId, memberId);
    }
}
