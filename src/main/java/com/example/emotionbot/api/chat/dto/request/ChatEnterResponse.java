package com.example.emotionbot.api.chat.dto.request;

import com.example.emotionbot.api.chat.entity.Sender;
import lombok.Builder;

@Builder
public record ChatEnterResponse(
        Long memberId,
        String message,
        Sender sender
) {
}
