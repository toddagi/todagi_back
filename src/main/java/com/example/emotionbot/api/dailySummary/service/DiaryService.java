package com.example.emotionbot.api.dailySummary.service;

import com.example.emotionbot.api.challenge.entity.ChallengeOption;
import com.example.emotionbot.api.challenge.service.ChallengeService;
import com.example.emotionbot.api.dailySummary.dto.req.DiaryRequest;
import com.example.emotionbot.api.dailySummary.dto.res.DiaryResponse;
import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.dailySummary.entity.Feeling;
import com.example.emotionbot.api.dailySummary.repository.DailySummaryRepository;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DailySummaryRepository dailySummaryRepository;
    private final MemberRepository memberRepository;
    private final ChallengeService challengeService;
    @Transactional
    public Long saveDiary(Long memberId, DiaryRequest diaryRequest) {
        Member member=memberRepository.findById(memberId).orElseThrow(()->new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        challengeService.completeChallenge(memberId, ChallengeOption.DIARY);

        DailySummary newDailySummary = DailySummary.builder()
                    .member(member)
                    .feeling(Feeling.fromValue(diaryRequest.feeling()))
                    .date(diaryRequest.date())
                    .diary(diaryRequest.diary())
                    .build();
            return dailySummaryRepository.save(newDailySummary).getId();
    }


    @Transactional
    @Cacheable(
            value = "diary",
            key = "T(String).valueOf(#memberId).concat(':').concat(#year).concat('-').concat(#month)",
            unless = "#result == null or #result.isEmpty()"
    )
    public List<DiaryResponse> getDailySummariesByMonth(int year,int month,Long memberId) {
        return dailySummaryRepository.findByMonth(year, month, memberId)
                .stream()
                .map(diary -> new DiaryResponse(
                        Feeling.toValue(diary.getFeeling().toString()),
                        diary.getDiary(),
                        diary.getDate(),
                        diary.getSummary(),
                        diary.getAngry(),
                        diary.getAnnoy(),
                        diary.getSleepy(),
                        diary.getGood(),
                        diary.getHappy()
                ))
                .toList();
    }
}
