package com.lof.game.implement;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lof.common.District;
import com.lof.game.domain.Game;
import com.lof.game.domain.GameMember;
import com.lof.game.domain.TeamColor;
import com.lof.game.domain.WinResult;
import com.lof.game.domain.WinVote;
import com.lof.game.repository.GameRepository;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.domain.RankTier;
import com.lof.member.implement.MemberManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GameManager {

    // TODO: 연승, 연패에 따른 점수 조정 고려
    private static final int CHANGE_AMOUNT = 20;

    private final GameRepository gameRepository;
    private final MemberManager memberManager;

    public long createGame(LocalDate date, RankTier rankTier, District district) {
        Game game = new Game(date, rankTier, district);
        gameRepository.save(game);

        return game.getId();
    }

    public long startGame(long gameId, Set<Long> memberIds) {
        List<Member> sortedMembers = memberManager.getMembers(memberIds)
                .stream()
                .sorted((m1, m2) -> m1.getRankScore() - m2.getRankScore())
                .collect(Collectors.toList());
        Game game = getGame(gameId);

        for (int i = 0; i < sortedMembers.size(); i++) {
            Member member = sortedMembers.get(i);
            TeamColor team = (i % 2 == 0) ? TeamColor.BLUE : TeamColor.RED;
            game.addGameMember(new GameMember(game, member, team));
        }
        game.startGame();
        return gameRepository.save(game).getId();
    }

    public Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_GAME));
    }

    public void deleteGame(long gameId) {
        gameRepository.deleteById(gameId);
    }
}
