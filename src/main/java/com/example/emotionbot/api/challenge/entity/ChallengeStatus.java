package com.example.emotionbot.api.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChallengeStatus {
    START(1), REWARD(2), END(3);
    private final int statusNum;
}
