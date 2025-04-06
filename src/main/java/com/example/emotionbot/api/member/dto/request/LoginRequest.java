package com.example.emotionbot.api.member.dto.request;

import lombok.Builder;

@Builder
public record LoginRequest(
        String loginId,
        String password
) {

}

