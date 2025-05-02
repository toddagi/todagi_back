package com.example.emotionbot.api.chat.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatSendResponse(
        List<String> message
) {
}
