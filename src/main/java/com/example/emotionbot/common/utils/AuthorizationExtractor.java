package com.example.emotionbot.common.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public class AuthorizationExtractor {
    private static final String HEADER_NAME = "Authorization";
    private static final String SCHEME = "Bearer";

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

}
