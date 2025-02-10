package com.lof.auth.service;

import org.springframework.stereotype.Service;

import com.lof.auth.domain.LoginToken;
import com.lof.auth.implement.TokenManager;
import com.lof.member.domain.Member;
import com.lof.member.implement.MemberReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberReader memberReader;
    private final TokenManager tokenManager;

    public LoginToken issueLoginToken(String loginId, String password) {
        Member member = memberReader.readLoginMember(loginId, password);
        return tokenManager.createLoginToken(member);
    }

    public LoginToken reissueLoginToken(String refreshToken) {
        long memberId = tokenManager.parseMemberId(refreshToken);
        Member member = memberReader.read(memberId);

        return tokenManager.createLoginToken(member);
    }
}
