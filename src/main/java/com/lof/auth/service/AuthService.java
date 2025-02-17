package com.lof.auth.service;

import org.springframework.stereotype.Service;

import com.lof.auth.implement.AuthRequestValidator;
import com.lof.auth.implement.TokenManager;
import com.lof.auth.implement.dto.LoginToken;
import com.lof.member.domain.Member;
import com.lof.member.implement.MemberDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberDao memberDao;
    private final AuthRequestValidator requestValidator;
    private final TokenManager tokenManager;

    public void signUp(Member member) {
        requestValidator.validateDuplicatedUsername(member.getUsername());
        memberDao.save(member);
    }

    public LoginToken issueLoginToken(String username, String password) {
        /*
        1. username에 해당하는 회원 찾기
        2. 회원 비밀번호 검증
        3. 회원 정보를 바탕으로 accessToken, refreshToken 발급받기
        4. 회원의 이전 refresh token이 존재한다면, 무효화시키기
         */
        Member member = memberDao.getMemberByUsername(username);
        requestValidator.validatePassword(member.getPassword(), password);

        return tokenManager.createLoginToken(member.getId());
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
        return tokenManager.reissueLoginToken(refreshToken);
    }
}
