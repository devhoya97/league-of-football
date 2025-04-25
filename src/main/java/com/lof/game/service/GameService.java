package com.lof.game.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lof.game.domain.Game;
import com.lof.game.domain.GameMember;
import com.lof.game.domain.TeamColor;
import com.lof.game.domain.WinResult;
import com.lof.game.domain.WinVote;
import com.lof.game.implement.GameManager;
import com.lof.game.repository.GameMemberRepository;
import com.lof.game.repository.GameRepository;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    // TODO: 연승, 연패에 따른 점수 조정 고려
    private static final int RANK_SCORE_CHANGE_AMOUNT = 20;
    private static final int MAX_VOTE_COUNT = 10;

    private final GameMemberRepository gameMemberRepository;
    private final GameRepository gameRepository;

    /**
     * GameInfo라는 DTO와 GameResponse라는 DTO는 현재 필드가 같은데 클래스를 나눠놨음
     * 그 이유는 API에서 사용하는 DTO는 API 응답 스펙과 직접적으로 연관되어 있으므로 service에서 사용하는 DTO를 분리하고 싶었기 때문임
     * 만약 같은 DTO를 사용한다면 API 스펙이 바뀔 때마다 service 코드도 DTO 때문에 변경되어야 함.
     * -> 그냥 서비스에서 DTO가 아닌 Game이라는 엔티티를 반환할까 생각해봤는데, 그럼 Lazy Loading 문제를 어떤 식으로 처리할지 고민됨. OSIV 관련
     */
    public GameInfo findGameInfo(long gameId) {
        Game game = getGame(gameId);

        List<Long> blueTeamMemberIds = getTeamMemberIds(game, TeamColor.BLUE);
        List<Long> redTeamMemberIds = getTeamMemberIds(game, TeamColor.RED);

        return new GameInfo(
                game.getDate(),
                game.getRankTier(),
                game.getDistrict(),
                game.getStatus(),
                game.getWinResult(),
                blueTeamMemberIds,
                redTeamMemberIds);
    }

    private List<Long> getTeamMemberIds(Game game, TeamColor teamColor) {
        return game.getGameMembers()
                .stream()
                .filter(gm -> gm.getTeam() == teamColor)
                .map(gm -> gm.getMember().getId())
                .collect(Collectors.toList());
    }

    // 10명이 모두 투표했으면 점수 변동 로직 가동
    // TODO: 경기 다음날 자정까지 투표를 안하면, 지금까지 투표 한 인원을 기준으로 승패 결과를 결정하는 로직 구현
    @Transactional
    public void submitWinVote(WinVote winVote, long gameId, long memberId) {
        List<GameMember> gameMembers = gameMemberRepository.findAllByGameId(gameId);
        GameMember gameMember = getGameMember(memberId, gameMembers);
        gameMember.voteWinTeam(winVote);
        int voteCount = calculateVoteCount(gameMembers);
        if (voteCount == MAX_VOTE_COUNT) {
            WinResult winResult = calculateWinResult(gameMembers);
            Game game = getGame(gameId); // 굳이 select 작업 안 하고 @Modify 적용하는게 나을까?
            game.determineWinResult(winResult);
            game.completeGame();
            if (winResult != WinResult.DRAW) {
                TeamColor winTeamColor = winResult.toTeamColor();
                List<Member> winTeamMembers = getTeamMembers(gameMembers, winTeamColor);
                List<Member> loseTeamMembers = getTeamMembers(gameMembers, winTeamColor.getOpposite());
                changeRankScore(winTeamMembers, true);
                changeRankScore(loseTeamMembers, false);
            }
        }
    }

    private GameMember getGameMember(long memberId, List<GameMember> gameMembers) {
        GameMember gameMember = gameMembers.stream()
                .filter(gm -> gm.getMember().getId() == memberId)
                .findFirst()
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_GAME_MEMBER));
        return gameMember;
    }

    private int calculateVoteCount(List<GameMember> gameMembers) {
        return (int) gameMembers.stream()
                .filter(gm -> gm.getWinVote() != WinVote.NOT_DETERMINED)
                .count();
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_GAME));
    }

    private WinResult calculateWinResult(List<GameMember> gameMembers) {
        int blueWinCount = calculateWinVoteCount(gameMembers, WinVote.BLUE);
        int redWinCount = calculateWinVoteCount(gameMembers, WinVote.RED);
        if (blueWinCount > redWinCount) {
            return WinResult.BLUE;
        }
        if (blueWinCount < redWinCount) {
            return WinResult.RED;
        }
        return WinResult.DRAW;
    }

    private int calculateWinVoteCount(List<GameMember> gameMembers, WinVote winVote) {
        return (int) gameMembers.stream()
                .filter(gm -> gm.getWinVote() == winVote)
                .count();
    }

    private List<Member> getTeamMembers(List<GameMember> gameMembers, TeamColor teamColor) {
        return gameMembers.stream()
                .filter(gm -> gm.getTeam() == teamColor)
                .map(GameMember::getMember)
                .toList();
    }

    private void changeRankScore(List<Member> members, boolean doesWin) {
        int changeAmount = RANK_SCORE_CHANGE_AMOUNT;
        if (!doesWin) {
            changeAmount *= -1;
        }
        for (Member member : members) {
            member.changeRankScore(changeAmount);
        }
    }
}
