package com.lof.game.service;

import java.time.LocalDate;
import java.util.List;

import com.lof.common.District;
import com.lof.game.domain.GameStatus;
import com.lof.game.domain.WinResult;
import com.lof.member.domain.RankTier;

public record GameInfo(
        LocalDate date,
        RankTier rankTier,
        District district,
        GameStatus status,
        WinResult winResult,
        List<Long> blueTeamMemberIds,
        List<Long> redTeamMemberIds
) {
}
