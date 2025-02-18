package com.lof.auth.implement;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenIssuer {

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

    public String createAccessToken(long memberId) {
        return createToken(memberId, accessTokenExpiration);
    }

    public String createRefreshToken(long memberId) {
        return createToken(memberId, refreshTokenExpiration);
    }

    private String createToken(long memberId, long expiration) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
