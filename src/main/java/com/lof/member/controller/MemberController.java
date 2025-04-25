package com.lof.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lof.member.service.MemberService;
import com.lof.member.service.RankTierAndScore;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}/rank")
    public RankResponse getMemberRank(@PathVariable long memberId) {
        RankTierAndScore rankTierAndScore = memberService.getMemberRank(memberId);

        return new RankResponse(rankTierAndScore);
    }
}
