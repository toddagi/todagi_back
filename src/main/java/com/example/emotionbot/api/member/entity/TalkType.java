package com.example.emotionbot.api.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TalkType {
    T("논리형", 1),
    F("위로형", 2),
    P("긍정형", 3);

    private final String talkTypeString;
    private final int talkTypeValue;
}
