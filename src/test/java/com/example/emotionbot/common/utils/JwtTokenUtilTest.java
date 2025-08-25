package com.example.emotionbot.common.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JwtTokenUtilV2.class)
class JwtTokenUtilTest {
    @Autowired
    private JwtTokenUtilV2 jwtTokenUtil;

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    @Test
    @DisplayName("토큰이 올바르게 생성된다.")
    void createToken() {
        final String payload=String.valueOf(1L);

        final String token=jwtTokenUtil.createToken(payload);

        assertNotNull(token);
    }

    @Test
    @DisplayName("올바른 토큰 정보로 payload를 조회한다.")
    void getPayloadByValidToken() {
        final String payload = String.valueOf(1L);

        final String token = jwtTokenUtil.createToken(payload);

        assertThat(jwtTokenUtil.verify(token).getSubject()).isEqualTo(payload);
    }

    @Test
    @DisplayName("유효하지 않은 토큰을 조회할 경우 예외가 발생한다.")
    void getInvalidToken(){
        JwtException exception = assertThrows(JwtException.class, () -> {
            jwtTokenUtil.verify(null);
        });

        assertThat(exception.getMessage()).isEqualTo("유효하지 않은 토큰입니다");
    }

    @Test
    @DisplayName("만료된 토큰을 조회할 경우 예외가 발생한다.")
    void getExpiredToken(){
        final String expiredToken= Jwts.builder()
                .setSubject(String.valueOf(1L))
                .setExpiration(new Date((new Date()).getTime() - 1))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();

        JwtException exception = assertThrows(JwtException.class, () -> {
            jwtTokenUtil.verify(expiredToken);
        });

        assertThat(exception.getMessage()).isEqualTo("유효기간이 만료된 토큰입니다");

    }

}