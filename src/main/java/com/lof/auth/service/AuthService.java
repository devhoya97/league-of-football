package com.lof.auth.service;

import org.springframework.stereotype.Service;

import com.lof.auth.implement.AuthRequestValidator;
import com.lof.auth.implement.TokenIssuer;
import com.lof.auth.implement.TokenParser;
import com.lof.auth.implement.TokenValidator;
import com.lof.auth.implement.dto.LoginToken;
import com.lof.member.domain.Member;
import com.lof.member.implement.MemberDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberDao memberDao;
    private final AuthRequestValidator requestValidator;
    private final TokenValidator tokenValidator;
    private final TokenIssuer tokenIssuer;
    private final TokenParser tokenParser;

    public void signUp(Member member) {
        requestValidator.validateDuplicatedUsername(member.getUsername());
        memberDao.save(member);
    }

    public LoginToken issueLoginToken(String username, String password) {
        Member member = memberDao.getMemberByUsername(username);
        requestValidator.validatePassword(member.getPassword(), password);

        return createLoginToken(member.getId());
    }

    public LoginToken reissueLoginToken(String refreshToken) {
        long memberId = tokenParser.parseMemberId(refreshToken);
        tokenValidator.validateRefreshToken(memberId, refreshToken);

        return createLoginToken(memberId);
    }

    private LoginToken createLoginToken(long memberId) {
        LoginToken loginToken = tokenIssuer.createLoginToken(memberId);
        tokenValidator.saveValidRefreshToken(memberId, loginToken.refreshToken());

        return loginToken;
    }

    public void logout(String refreshToken) {
        long memberId = tokenParser.parseMemberId(refreshToken);
        tokenValidator.invalidatePreviousRefreshToken(memberId, refreshToken);
    }
}
