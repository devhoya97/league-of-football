package com.lof.member.service;

import com.lof.member.domain.RankTier;

public record RankTierAndScore(
        RankTier rankTier,
        int rankScore
) {
}
