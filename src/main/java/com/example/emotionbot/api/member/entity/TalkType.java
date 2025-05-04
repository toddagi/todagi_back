package com.example.emotionbot.api.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TalkType {
    T("위로형"),
    F("논리분석형"),
    FUNNY_FRIEND("유쾌한친구형"),
    GIRLFRIEND("여자친구형"),
    BOYFRIEND("남자친구형");

    private final String talkTypeString;
}
