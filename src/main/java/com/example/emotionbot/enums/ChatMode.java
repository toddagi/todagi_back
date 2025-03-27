package com.example.emotionbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatMode {
    THINKING("T"),
    FEELING("F");
    private final String type;
}
