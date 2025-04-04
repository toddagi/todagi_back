package com.example.emotionbot.api.jwt.service;

import com.example.emotionbot.api.member.dto.response.LoginResponse;
import com.example.emotionbot.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;
    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;

    public LoginResponse generateToken(final Long memberId) {
        final String accessToken = jwtUtil.generateAccessToken(memberId);
        final String refreshToken = jwtUtil.generateRefreshToken(memberId);
        saveRefreshToken(memberId, refreshToken);
        return LoginResponse.of(accessToken, refreshToken);
    }

    private void saveRefreshToken(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(
                "refreshToken:" + memberId,
                refreshToken,
                REFRESH_TOKEN_EXPIRATION,
                TimeUnit.SECONDS
        );
    }

    public String getRefreshToken(Long userId) {
        return redisTemplate.opsForValue().get("refreshToken:" + userId);
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete("refreshToken:" + userId);
    }
}
