package com.example.emotionbot.api.member.dto.req;

import com.example.emotionbot.api.member.entity.TalkType;
import com.example.emotionbot.api.member.entity.KeyboardYn;
import lombok.Getter;

@Getter
public class SignUpReqDto {
    private String loginId;
    private String password;
    private String nickname;
    private KeyboardYn keyboardYn;
    private TalkType talkType;
}
