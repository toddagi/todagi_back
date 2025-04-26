package com.example.emotionbot.api.challenge.service;

import com.example.emotionbot.api.challenge.entity.Challenge;
import com.example.emotionbot.api.challenge.entity.ChallengeOption;
import com.example.emotionbot.api.challenge.entity.ChallengeStatus;
import com.example.emotionbot.api.challenge.repository.ChallengeRepository;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.example.emotionbot.api.challenge.entity.ChallengeOption.*;
import static com.example.emotionbot.api.challenge.entity.ChallengeStatus.START;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public void createChallenge(Long memberId){
        Member member=memberRepository.findById(memberId).orElseThrow(()->new EmotionBotException(FailMessage.CONFLICT_NO_ID));
        LocalDate date = LocalDate.now();

        Challenge challengeAttendance = Challenge.builder()
            .member(member).challengeOption(ATTENDANCE).challengeStatus(START).localDate(date).build();
        challengeRepository.save(challengeAttendance);

        Challenge challengeCheckEmotion = Challenge.builder()
                .member(member).challengeOption(CHECK_SUMMARY).challengeStatus(START).localDate(date).build();
        challengeRepository.save(challengeCheckEmotion);

        Challenge challengeMissionComplete = Challenge.builder()
                .member(member).challengeOption(MISSION_COMPLETE).challengeStatus(START).localDate(date).build();
        challengeRepository.save(challengeMissionComplete);

        Challenge challengeChat = Challenge.builder()
                .member(member).challengeOption(CHAT).challengeStatus(START).localDate(date).build();
        challengeRepository.save(challengeChat);

        Challenge challengeDairy = Challenge.builder()
                .member(member).challengeOption(DIARY).challengeStatus(START).localDate(date).build();
        challengeRepository.save(challengeDairy);
    }

    @Transactional
    public void completeChallenge(Long memberId, ChallengeOption challengeOption) {
        Challenge challenge = challengeRepository.findByMemberIdAndChallengeOption(memberId, challengeOption)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_CHALLENGE));
        challenge.updateChallengeStatus(ChallengeStatus.REWARD);
    }

}
