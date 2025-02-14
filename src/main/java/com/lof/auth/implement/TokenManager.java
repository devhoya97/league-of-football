package com.lof.auth.implement;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lof.global.exception.AuthException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class TokenManager {

    private static final long MILLIS_TO_SECONDS = 1000L;

    private final String secret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public TokenManager(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration.access}") long accessTokenExpiration,
            @Value("${jwt.expiration.refresh}") long refreshTokenExpiration
    ) {
        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String createAccessToken(Member member) {
        return createToken(member, accessTokenExpiration);
    }

    public String createRefreshToken(Member member) {
        return createToken(member, refreshTokenExpiration);
    }

    private String createToken(Member member, long expiration) {
        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public long parseMemberId(String token) {
        Claims claims = parseClaims(token);

        return Long.parseLong(claims.getSubject());
    }

    public int parseExpirationSeconds(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();

        long now = System.currentTimeMillis();
        long expirationMillis = expiration.getTime() - now;

        return (int) (expirationMillis / MILLIS_TO_SECONDS);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorCode.EXPIRED_TOKEN);
        }
    }
}
