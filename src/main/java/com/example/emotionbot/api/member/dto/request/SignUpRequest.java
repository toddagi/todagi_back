package com.example.emotionbot.api.member.dto.request;

import com.example.emotionbot.api.member.entity.TalkType;
import com.example.emotionbot.api.member.entity.KeyboardYn;
import lombok.Builder;

@Builder
public record SignUpRequest(
        String loginId,
        String password,
        String nickname,
        KeyboardYn keyboardYn,
        TalkType talkType) {
}
