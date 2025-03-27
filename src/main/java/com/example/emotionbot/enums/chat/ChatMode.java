package com.example.emotionbot.enums.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatMode {
    THINKING("T"),
    FEELING("F");
    private final String type;
}
