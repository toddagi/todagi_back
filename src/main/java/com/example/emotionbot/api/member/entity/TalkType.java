package com.example.emotionbot.api.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TalkType {
    T("위로형", 1),
    F("논리분석형", 2),
    FUNNY_FRIEND("유쾌한친구형", 3),
    GIRLFRIEND("여자친구형", 4),
    BOYFRIEND("남자친구형", 5);

    private final String talkTypeString;
    private final int talkTypeValue;
}
