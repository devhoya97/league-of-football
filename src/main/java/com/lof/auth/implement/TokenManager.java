package com.lof.auth.implement;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lof.auth.domain.InvalidRefreshToken;
import com.lof.auth.domain.LoginToken;
import com.lof.auth.domain.ValidRefreshToken;
import com.lof.auth.repository.InvalidRefreshTokenRepository;
import com.lof.auth.repository.ValidRefreshTokenRepository;
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

    private static final int MILLIS_TO_SECONDS = 1000;

    private final String secret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final ValidRefreshTokenRepository validRefreshTokenRepository;
    private final InvalidRefreshTokenRepository invalidRefreshTokenRepository;

    public TokenManager(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration.access}") long accessTokenExpiration,
            @Value("${jwt.expiration.refresh}") long refreshTokenExpiration,
            ValidRefreshTokenRepository validRefreshTokenRepository,
            InvalidRefreshTokenRepository invalidRefreshTokenRepository
    ) {
        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.validRefreshTokenRepository = validRefreshTokenRepository;
        this.invalidRefreshTokenRepository = invalidRefreshTokenRepository;
    }

    // TODO: 일련의 과정들이 하나의 트랜잭션으로 묶여서 수행되면 좋긴 할텐데 Redis에서도 가능할까?
    //  사실 하나의 트랜잭션으로 묶이지 않아서 부분적으로 실패하더라도, 유저가 재로그인 하면 되는거라 큰 무리는 없을 것 같다.
    // TODO: redis에 저장할 때, refreshToken 자체의 남은 expire 기간과 똑같이 redis expire 설정하기
    public LoginToken createLoginToken(Member member) {
        invalidatePreviousRefreshToken(member);
        String refreshToken = createToken(member, refreshTokenExpiration);

        validRefreshTokenRepository.save(new ValidRefreshToken(member.getId(), refreshToken, refreshTokenExpiration / MILLIS_TO_SECONDS));
        String accessToken = createToken(member, accessTokenExpiration);

        return new LoginToken(accessToken, refreshToken);
    }

    private void invalidatePreviousRefreshToken(Member member) {
        validRefreshTokenRepository.findById(member.getId())
                .ifPresent((validRefreshToken) -> {
                    validRefreshTokenRepository.deleteById(member.getId());
                    long remainingExpirationMillis = parseExpirationMillis(validRefreshToken.getRefreshToken());
                    invalidRefreshTokenRepository.save(new InvalidRefreshToken(validRefreshToken, remainingExpirationMillis / MILLIS_TO_SECONDS));
                });
    }

    private String createToken(Member member, long expiration) {
        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void validateRefreshToken(String refreshToken) {
        invalidRefreshTokenRepository.findById(refreshToken)
                .ifPresent((invalidRefreshToken) -> {
                    throw new AuthException(ErrorCode.INVALID_TOKEN);
                });
    }

    public long parseMemberId(String token) {
        Claims claims = parseClaims(token);

        return Long.parseLong(claims.getSubject());
    }

    private long parseExpirationMillis(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();

        long now = System.currentTimeMillis();

        return expiration.getTime() - now;
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
