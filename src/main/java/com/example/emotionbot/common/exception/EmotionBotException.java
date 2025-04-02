package com.example.emotionbot.common.exception;

import lombok.Getter;

@Getter
public class EmotionBotException extends RuntimeException {
    private final FailMessage failMessage;

    public EmotionBotException(final FailMessage failMessage) {
        super(failMessage.getMessage());
        this.failMessage = failMessage;
    }
}
