package com.example.emotionbot.api.challenge.service;

import com.example.emotionbot.api.challenge.dto.RewardChallengeRequest;
import com.example.emotionbot.api.challenge.dto.checkChallengeResponse;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.emotionbot.api.challenge.entity.ChallengeOption.*;
import static com.example.emotionbot.api.challenge.entity.ChallengeStatus.START;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public void createChallenge(Long memberId) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        LocalDate date = LocalDate.now();

        List<Challenge> challenges = List.of(
                Challenge.builder().member(member).challengeOption(ATTENDANCE).challengeStatus(START).localDate(date).build(),
                Challenge.builder().member(member).challengeOption(CHECK_SUMMARY).challengeStatus(START).localDate(date).build(),
                Challenge.builder().member(member).challengeOption(MISSION_COMPLETE).challengeStatus(START).localDate(date).build(),
                Challenge.builder().member(member).challengeOption(CHAT).challengeStatus(START).localDate(date).build(),
                Challenge.builder().member(member).challengeOption(DIARY).challengeStatus(START).localDate(date).build()
        );

        challengeRepository.saveAll(challenges);
    }

    @Transactional
    public List<checkChallengeResponse> checkChallenge(Long memberId) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        List<Challenge> challenges = challengeRepository.findAllByMemberId(memberId);

        return challenges.stream()
                .map(challenge -> new checkChallengeResponse(
                        challenge.getChallengeOption().getChallengeNum(),
                        challenge.getChallengeStatus().getStatusNum()
                ))
                .toList();
    }

    @Transactional
    public void completeChallenge(Long memberId, ChallengeOption challengeOption) {
        Challenge challenge = challengeRepository.findByMemberIdAndChallengeOption(memberId, challengeOption)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_CHALLENGE));

        if (challenge.getChallengeStatus() == ChallengeStatus.START) {
            challenge.updateChallengeStatus(ChallengeStatus.REWARD);
        }
    }

    @Transactional
    public void rewardChallenge(Long memberId, RewardChallengeRequest rewardChallengeRequest) {
        int challengeNum = rewardChallengeRequest.challengeNum();
        ChallengeOption challengeOption = findChallengeOptionByNum(challengeNum);

        Challenge challenge = challengeRepository.findByMemberIdAndChallengeOption(memberId, challengeOption)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_CHALLENGE));
        challenge.updateChallengeStatus(ChallengeStatus.END);

        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));
        member.updateClover(challengeOption.getCoin());
    }

    private ChallengeOption findChallengeOptionByNum(int challengeNum) {
        for (ChallengeOption option : ChallengeOption.values()) {
            if (option.getChallengeNum() == challengeNum) {
                return option;
            }
        }
        throw new EmotionBotException(FailMessage.NOT_FOUND);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetChallengesAt3AM() {
        resetAllChallengesToStart();
    }

    @Transactional
    public void resetAllChallengesToStart() {
        List<Long> allMemberId = memberRepository.findAllIds();

        for (Long memberId : allMemberId) {
            updateMissionComplete(memberId);
        }

        List<Challenge> allChallenges = challengeRepository.findAllWithMember();
        for (Challenge challenge : allChallenges) {
            if (challenge.getChallengeStatus() == ChallengeStatus.REWARD) {
                Member member = challenge.getMember();
                member.updateClover(challenge.getChallengeOption().getCoin());
            }
            challenge.updateChallengeStatus(ChallengeStatus.START);
        }
    }

    @Transactional
    public void updateMissionComplete(Long memberId) {
        List<Challenge> challenges = challengeRepository.findAllByMemberId(memberId);

        Challenge missionCompleteChallenge = challenges.stream()
                .filter(challenge -> challenge.getChallengeOption() == ChallengeOption.MISSION_COMPLETE)
                .findFirst()
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_CHALLENGE));

        boolean allOtherChallengesNotStart = challenges.stream()
                .filter(challenge -> challenge.getChallengeOption() != ChallengeOption.MISSION_COMPLETE)
                .allMatch(challenge -> challenge.getChallengeStatus() != ChallengeStatus.START);

        if (allOtherChallengesNotStart && missionCompleteChallenge.getChallengeStatus() == ChallengeStatus.START) {
            missionCompleteChallenge.updateChallengeStatus(ChallengeStatus.REWARD);
        }
    }

}
