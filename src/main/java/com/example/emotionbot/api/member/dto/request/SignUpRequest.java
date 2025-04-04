package com.example.emotionbot.api.member.dto.request;

import com.example.emotionbot.api.member.dto.response.LoginResponse;
import com.example.emotionbot.enums.chat.ChatMode;
import com.example.emotionbot.enums.keyboard.KeyboardYn;
import lombok.Builder;

@Builder
public record SignUpRequest(
    String loginId,
    String password,
    String nickname,
    KeyboardYn keyboardYn,
    ChatMode chatMode
){
}
