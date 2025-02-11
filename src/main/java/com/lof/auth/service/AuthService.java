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

    //  1. ValidRefreshToken에 이미 값이 있다면, InvalidRefreshToken으로 이동시킨다.
    //  2. 새로 발급한 refreshToken을 ValidRefreshToken에 넣는다.
    public LoginToken issueLoginToken(String loginId, String password) {
        Member member = memberReader.readLoginMember(loginId, password);
        LoginToken loginToken = tokenManager.createLoginToken(member);

        return loginToken;
    }

    //  1. ValidRefreshToken에 refreshToken이 존재하는지 확인한다.
    //  2. ValidRefreshToken에 값이 있다면, InvalidRefreshToken으로 이동시킨다. -> 없어도 JWT parse를 성공했으니 인정하는 방향으로
    //  3. 새로 발급한 refreshToken을 ValidRefreshToken에 넣는다.
    public LoginToken reissueLoginToken(String refreshToken) {
        tokenManager.validateRefreshToken(refreshToken);
        long memberId = tokenManager.parseMemberId(refreshToken);
        Member member = memberReader.read(memberId);

        return tokenManager.createLoginToken(member);
    }
}
