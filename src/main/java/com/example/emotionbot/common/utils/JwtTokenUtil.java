package com.example.emotionbot.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {
    private static final String HEADER_NAME = "Authorization";
    private static final String SCHEME = "Bearer";
    private final UserDetailsService userDetailsService;
    @Value("${spring.jwt.token.access-expiration-time}")
    private Long expirationMillis;

    @Value("${spring.jwt.secret-key}")
    private String secretKey;
    private SecretKey key;

    //문자열 추출
    public static String extract(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_NAME);
        if (!Objects.isNull(authorization)
                && authorization.toLowerCase().startsWith(SCHEME.toLowerCase())) {
            String tokenValue = authorization.substring(SCHEME.length()).trim();
            int commaIndex = tokenValue.indexOf(',');
            if (commaIndex > 0) {
                tokenValue = tokenValue.substring(0, commaIndex);
            }
            return tokenValue;
        }
        return null;
    }

    @PostConstruct
    public void initialize() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);
        try {
            return Jwts.builder()
                    .setSubject(subject)
                    .setExpiration(expiration)
                    .signWith(key)
                    .compact();
        } catch (JwtException e) {
            throw new JwtException("토큰 생성중 오류가 발생했습니다.");
        }
    }

    public Authentication getAuthentication(String token) {
        String email = verify(token).getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    //토큰이 유효한 지 검증
    public Claims verify(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
    }
}
