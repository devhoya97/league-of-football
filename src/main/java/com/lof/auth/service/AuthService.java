package com.lof.auth.service;

import org.springframework.stereotype.Service;

import com.lof.auth.service.dto.LoginToken;
import com.lof.auth.implement.TokenManager;
import com.lof.auth.implement.TokenValidator;
import com.lof.member.domain.Member;
import com.lof.member.implement.MemberReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberReader memberReader;
    private final TokenManager tokenManager;
    private final TokenValidator tokenValidator;

    public LoginToken issueLoginToken(String loginId, String password) {
        /*
        1. 로그인 정보에 해당하는 회원 찾기
        2. 회원 정보를 바탕으로 accessToken, refreshToken 발급받기
        3. 회원의 이전 refresh token이 존재한다면, 무효화시키기
         */
        Member member = memberReader.login(loginId, password);
        String accessToken = tokenManager.createAccessToken(member);
        String refreshToken = tokenManager.createRefreshToken(member);
        tokenValidator.invalidatePreviousRefreshToken(member);

        return new LoginToken(accessToken, refreshToken);
    }

    // TODO: 일련의 과정들이 하나의 트랜잭션으로 묶여서 수행되면 좋긴 할텐데 Redis에서도 가능할까?
    //  사실 하나의 트랜잭션으로 묶이지 않아서 부분적으로 실패하더라도, 유저가 재로그인 하면 되는거라 큰 무리는 없을 것 같다.
    public LoginToken reissueLoginToken(String refreshToken) {
        /*
        1. refreshToken이 유효한지 확인
        2. refreshToken으로부터 회원id 파싱
        3. refreshToken을 무효화시키기
        4. 회원 정보를 바탕으로 새로운 accessToken, refreshToken 발급받기
         */
        tokenValidator.validateRefreshToken(refreshToken);
        long memberId = tokenManager.parseMemberId(refreshToken);
        Member member = memberReader.read(memberId);
        tokenValidator.invalidatePreviousRefreshToken(member);
        String accessToken = tokenManager.createAccessToken(member);
        String newRefreshToken = tokenManager.createRefreshToken(member);

        return new LoginToken(accessToken, newRefreshToken);
    }
}
