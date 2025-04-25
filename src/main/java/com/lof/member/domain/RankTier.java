package com.lof.member.domain;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

public enum RankTier {
    DIAMOND_1(2200),
    DIAMOND_2(2100),
    DIAMOND_3(2000),
    PLATINUM_1(1900),
    PLATINUM_2(1800),
    PLATINUM_3(1700),
    GOLD_1(1600),
    GOLD_2(1500),
    GOLD_3(1400),
    SILVER_1(1300),
    SILVER_2(1200),
    SILVER_3(1100),
    BRONZE_1(1000),
    BRONZE_2(900),
    BRONZE_3(800);

    private final int minRankScore;

    RankTier(int minRankScore) {
        this.minRankScore = minRankScore;
    }

    public static RankTier findByRankScore(int rankScore) {
        return Arrays.stream(RankTier.values())
                .filter(rankTier -> rankTier.minRankScore <= rankScore)
                .findFirst()
                .orElse(BRONZE_3);
    }

    public int getMinRankScore() {
        return minRankScore;
    }

    @JsonCreator
    public static RankTier from(String input) {
        return Arrays.stream(RankTier.values())
                .filter(r -> r.name().equals(input))
                .findFirst()
                .orElseThrow(() -> new BizException(ErrorCode.INVALID_RANK_TIER));
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
