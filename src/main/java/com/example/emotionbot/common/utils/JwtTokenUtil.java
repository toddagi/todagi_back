package com.example.emotionbot.common.utils;

import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
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
import java.nio.charset.StandardCharsets;
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
    private Long accessExpirationMillis;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private Long refreshExpirationMillis;

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    private SecretKey key;

    public static String extract(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_NAME);
        return parseBearerToken(authorization);
    }

    private static String parseBearerToken(String headerValue) {
        if (Objects.isNull(headerValue) || !headerValue.toLowerCase().startsWith(SCHEME.toLowerCase())) {
            return null;
        }

        String token = headerValue.substring(SCHEME.length()).trim();
        int commaIndex = token.indexOf(',');
        return (commaIndex > 0) ? token.substring(0, commaIndex) : token;
    }

    @PostConstruct
    public void initialize() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String extractTokenValue(String bearerToken) {
        validateTokenValue(bearerToken);
        validateBearerScheme(bearerToken);
        return parseBearerToken(bearerToken);
    }

    private void validateTokenValue(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank() || bearerToken.trim().equalsIgnoreCase(SCHEME)) {
            log.warn("Authorization 헤더 값이 없습니다.");
            throw new EmotionBotException(FailMessage.UNAUTHORIZED_EMPTY_HEADER);
        }
    }

    private void validateBearerScheme(String bearerToken) {
        if (!bearerToken.toLowerCase().startsWith(SCHEME.toLowerCase())) {
            log.warn("Authorization 헤더가 Bearer로 시작하지 않습니다: {}", bearerToken);
            throw new EmotionBotException(FailMessage.UNAUTHORIZED_INVALID_TOKEN);
        }
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

    public Authentication getAuthentication(String token) {
        String loginId = verify(token).getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

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

    public long getRefreshTokenExpirationMillis() {
        return refreshExpirationMillis;
    }
}
