package com.example.emotionbot.api.chat.dto.request;

import lombok.Builder;

@Builder
public record ChatSendRequestToAI(Long memberId,
                                  String message,
                                  String talkType) {
}
