package com.lof.auth.implement;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lof.auth.domain.TokenType;
import com.lof.auth.implement.dto.LoginToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenIssuer {

    static final String TOKEN_TYPE_CLAIM = "tokenType";

    private final String secret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public TokenIssuer(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration.access}") long accessTokenExpiration,
            @Value("${jwt.expiration.refresh}") long refreshTokenExpiration
    ) {
        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public LoginToken createLoginToken(long memberId) {
        String accessToken = createToken(memberId, accessTokenExpiration, TokenType.ACCESS);
        String refreshToken = createToken(memberId, refreshTokenExpiration, TokenType.REFRESH);

        return new LoginToken(accessToken, refreshToken);
    }

    private String createToken(long memberId, long expiration, TokenType tokenType) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
