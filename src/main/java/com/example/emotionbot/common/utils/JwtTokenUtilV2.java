package com.example.emotionbot.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtilV2 {

    @Value("${spring.jwt.token.access-expiration-time}")
    private Long accessExpirationMillis;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private Long refreshExpirationMillis;

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    private SecretKey key;

    @PostConstruct
    public void initialize() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String subject) {
        return generateToken(subject, accessExpirationMillis, "access");
    }

    public String createRefreshToken(String subject) {
        return generateToken(subject, refreshExpirationMillis, "refresh");
    }

    private String generateToken(String subject, Long expirationTime, String type) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);
        try {
            return Jwts.builder()
                    .setSubject(subject)
                    .setExpiration(expiration)
                    .signWith(key)
                    .compact();
        } catch (JwtException e) {
            throw new JwtException(String.format("%s 토큰 생성 중 오류가 발생했습니다.", type));
        }
    }

    public Claims verify(String token) {
        try {
            Claims claims= Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            validateExpiredToken(claims);
            return claims;
        }  catch (ExpiredJwtException e) {
            throw new JwtException("유효기간이 만료된 토큰입니다");
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("유효하지 않은 토큰입니다");
        }
    }

    private void validateExpiredToken(Claims claims) {
        if(claims.getExpiration().before(new Date())) {
            throw new JwtException("유효기간이 만료된 토큰입니다");
        }
    }

    public String getPayload(String token) {
        return verify(token).getSubject();
    }


    public long getRefreshTokenExpirationMillis() {
        return refreshExpirationMillis;
    }
}
