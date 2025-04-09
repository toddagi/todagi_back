package com.example.emotionbot.api.chat.dto.request;

import com.example.emotionbot.api.chat.entity.Sender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public record ChatSendResponse(
        Long memberId,
        String message,
        Sender sender
) {
}
