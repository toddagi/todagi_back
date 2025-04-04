package com.example.emotionbot.common.auth;

import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BearerAuthExtractor {
    private static final String HEADER_NAME = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    public static String extract(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_NAME);
        if (authorization == null) return null;
        return new BearerAuthExtractor().extractTokenValue(authorization);
    }

    public String extractTokenValue(final String bearerToken) {
        validateTokenValue(bearerToken);
        validateBearerKey(bearerToken);

        return bearerToken.substring(BEARER_TYPE.length()).trim();
    }

    private void validateTokenValue(final String bearerToken) {
        if (bearerToken == null || bearerToken.isEmpty() || bearerToken.trim().equals(BEARER_TYPE)) {
            log.warn("Autherizaiton 헤더 값이 없습니다.");
            throw new EmotionBotException(FailMessage.UNAUTHORIZED_EMPTY_HEADER);
        }
    }

    private void validateBearerKey(final String bearerToken) {
        if (!bearerToken.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            log.warn("AuthHeader의 값이 Bearer로 시작하지 않습니다: {}", bearerToken);
            throw new EmotionBotException(FailMessage.UNAUTHORIZED_INVALID_TOKEN);
        }
    }
}
