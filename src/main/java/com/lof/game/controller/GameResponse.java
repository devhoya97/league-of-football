package com.lof.game.controller;

import java.time.LocalDate;
import java.util.List;

import com.lof.common.District;
import com.lof.game.domain.GameStatus;
import com.lof.game.domain.WinResult;
import com.lof.game.service.GameInfo;
import com.lof.member.domain.RankTier;

public record GameResponse(
        LocalDate date,
        RankTier rankTier,
        District district,
        GameStatus status,
        WinResult winResult,
        List<Long> blueTeamMembers,
        List<Long> redTeamMembers
) {

    public GameResponse(GameInfo gameInfo) {
        this(
                gameInfo.date(),
                gameInfo.rankTier(),
                gameInfo.district(),
                gameInfo.status(),
                gameInfo.winResult(),
                gameInfo.blueTeamMemberIds(),
                gameInfo.redTeamMemberIds());
    }
}
