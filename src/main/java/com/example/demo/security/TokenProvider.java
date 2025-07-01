package com.example.demo.security;

import com.example.demo.model.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {

    // ✅ 최소 32바이트 이상인 시크릿 키 필요 (HS512 기준)
    private static final String SECRET_KEY = "rHC46RLCqUL7FSlWEm6EVrmdEuQeAW9CBnEDaPqgIuI=";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    // ✅ SecretKey 객체로 고정 생성
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 토큰 생성
     */
    public String create(UserEntity user) {
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .setSubject(user.getId()) // 유저 식별값
                .setIssuedAt(new Date()) // 발행 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS512) // ✅ 서명
                .compact();
    }

    public String create(final Authentication authentication) {
        ApplicationOAuth2User userPrincipal=(ApplicationOAuth2User) authentication.getPrincipal();

        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .setSubject(userPrincipal.getName()) // 유저 식별값
                .setIssuedAt(new Date()) // 발행 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS512) // ✅ 서명
                .compact();
    }

    /**
     * 토큰 검증 및 사용자 ID 추출
     */
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // ✅ 같은 key 객체 사용
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // userId
    }
}