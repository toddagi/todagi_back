package com.example.emotionbot.api.member.dto.response;

import com.example.emotionbot.api.member.entity.KeyboardYn;
import com.example.emotionbot.api.member.entity.TalkType;

public record MemberInformationResponse(Long memberId, String nickname, int clover, KeyboardYn keyboardYn,
                                        TalkType talkType) {
}
