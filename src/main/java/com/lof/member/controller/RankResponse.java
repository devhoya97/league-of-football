package com.lof.member.controller;

import com.lof.member.domain.RankTier;
import com.lof.member.service.RankTierAndScore;

public record RankResponse(
        RankTier rankTier,
        int rankScore
) {

    public RankResponse(RankTierAndScore rankTierAndScore) {
        this(rankTierAndScore.rankTier(), rankTierAndScore.rankScore());
    }
}
