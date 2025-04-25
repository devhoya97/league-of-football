package com.lof.member.service;

import org.springframework.stereotype.Service;

import com.lof.member.domain.Member;
import com.lof.member.domain.RankTier;
import com.lof.member.implement.MemberManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberManager memberManager;

    public RankTierAndScore getMemberRank(long memberId) {
        Member member = memberManager.getMemberById(memberId);
        int rankScore = member.getRankScore();

        return new RankTierAndScore(RankTier.findByRankScore(rankScore), rankScore);
    }
}
