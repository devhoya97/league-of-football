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
        Member member = memberDao.getMemberByUsername(username);
        requestValidator.validatePassword(member.getPassword(), password);

        return tokenManager.createLoginToken(member.getId());
    }

    public LoginToken reissueLoginToken(long memberId, String refreshToken) {
        return tokenManager.reissueLoginToken(memberId, refreshToken);
    }

    public void logout(long memberId, String refreshToken) {
        tokenManager.invalidateRefreshToken(memberId, refreshToken);
    }
}
