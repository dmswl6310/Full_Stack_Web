package com.example.demo.security;

import com.example.demo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "eko3aaaaaaaaaaaaaaaaafdwwwwwwwwwwwwwwwwwwwwwafdwafwsfoewskfoeejwifleiwlfhliiwhfi2les5ifeshfleisfeslfhsifehf";

    public String create(UserEntity userEntity) {
        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                    .plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
        return Jwts.builder()
                // header내용과 서명을 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(userEntity.getId()) // sub
                .setIssuer("demo app")  // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate)  // exp
                .compact();
    }

    public String validateAndGetUserId(String token) {
        // 1. Key 준비 (최소 256비트 → 32바이트 이상 문자열 필요)
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        // 2. ParserBuilder 사용
        JwtParser parser = Jwts
                .parser()
                .verifyWith(key) // 0.12.6부터는 verifyWith로 서명 키 설정
                .build();

        // 3. 파싱 및 subject 추출
        Claims claims = parser
                .parseSignedClaims(token) // 예전의 parseClaimsJws에 해당
                .getPayload();

        return claims.getSubject(); // subject는 일반적으로 userId 같은 것
    }
}

