package com.lof.auth.implement;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class TokenParser {

    private static final long MILLIS_TO_SECONDS = 1000L;

    private final String secret;

    public TokenParser(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
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
            throw new BizException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new BizException(ErrorCode.EXPIRED_TOKEN);
        }
    }
}
