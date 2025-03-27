package com.example.emotionbot.common.exception;

import com.example.emotionbot.enums.message.FailMessage;
import lombok.Getter;

@Getter
public class EmotionBotException extends RuntimeException{
    private final FailMessage failMessage;

    public EmotionBotException(final FailMessage failMessage) {
        super(failMessage.getMessage());
        this.failMessage = failMessage;
    }
}
