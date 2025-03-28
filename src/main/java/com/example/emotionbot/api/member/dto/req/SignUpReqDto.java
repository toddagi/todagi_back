package com.example.emotionbot.api.member.dto.req;

import com.example.emotionbot.enums.chat.ChatMode;
import com.example.emotionbot.enums.keyboard.KeyboardYn;
import lombok.Getter;

@Getter
public class SignUpReqDto {
    private String loginId;
    private String password;
    private String nickname;
    private int clover;
    private KeyboardYn keyboardYn;
    private ChatMode chatMode;
}
