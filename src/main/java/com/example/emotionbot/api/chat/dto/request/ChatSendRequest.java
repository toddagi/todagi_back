package com.example.emotionbot.api.chat.dto.request;

import com.example.emotionbot.api.chat.entity.ChatType;
import com.example.emotionbot.api.chat.entity.Sender;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatSendRequest(
        Long memberId,
        LocalDateTime sendTime,
        String message,
        ChatType type,
        Sender sender
) {
}
