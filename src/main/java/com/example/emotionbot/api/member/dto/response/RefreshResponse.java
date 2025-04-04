package com.example.emotionbot.api.member.dto.response;

import lombok.Builder;

@Builder
public record RefreshResponse(
        String accessToken
) {

    public static RefreshResponse of(final String accessToken) {
        return RefreshResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}

