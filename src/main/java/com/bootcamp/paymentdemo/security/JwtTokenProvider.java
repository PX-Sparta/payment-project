package com.bootcamp.paymentdemo.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // 1. 기존 메서드 (일반 로그인 등에서 사용 - 이메일 없음)
    public String generateAccessToken(Long id) {
        return generate(id, null, accessTokenExpiration);
    }

    // 2. 추가된 오버로딩 메서드 (OAuth2SuccessHandler 등에서 사용 - 이메일 포함)
    public String generateAccessToken(Long id, String email) {
        return generate(id, email, accessTokenExpiration);
    }

    public String generateRefreshToken(Long id) {
        return generate(id, null, refreshTokenExpiration);
    }

    public Long extractCustomerId(String token) {
        String subject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return Long.parseLong(subject);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 3. 코어 생성 로직: email 파라미터 추가 및 클레임 적용
    private String generate(Long id, String email, long expiration) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(secretKey);

        // 이메일이 전달되었다면 커스텀 클레임에 추가
        if (email != null && !email.trim().isEmpty()) {
            builder.claim("email", email);
        }

        return builder.compact();
    }
}