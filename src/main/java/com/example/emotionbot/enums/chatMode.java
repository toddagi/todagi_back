package com.example.emotionbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum chatMode {
    Thinking("T"),
    Feeling("F");
    private final String type;
}
