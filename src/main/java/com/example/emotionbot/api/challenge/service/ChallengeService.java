package com.example.emotionbot.api.challenge.service;

import com.example.emotionbot.api.challenge.entity.Challenge;
import com.example.emotionbot.api.challenge.entity.ChallengeOption;
import com.example.emotionbot.api.challenge.entity.ChallengeStatus;
import com.example.emotionbot.api.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {
    void createChallenge(Long memberId){
        //Challenge challenge(Member member, ChallengeOption challengeOption, ChallengeStatus challenge);
    }
}
