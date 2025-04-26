package com.example.emotionbot.api.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChallengeOption {
    ATTENDANCE("출석하기",1,1),
    CHECK_SUMMARY("요약 확인하기",1,2),
    MISSION_COMPLETE("미션 모두 완료하기",3,3),
    CHAT("덕덕이와 대화하기",2,4),
    DIARY("일기 작성하기",2,5);

    private final String title;
    private final int coin;
    private final int challengeNum;
}
