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
import com.example.emotionbot.common.utils.DateFormatUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DailySummaryRepository dailySummaryRepository;
    private final MemberRepository memberRepository;
    private final ChallengeService challengeService;
    private final DateFormatUtil dateFormatutil;

    @Transactional
    public Long saveDiary(Long memberId, DiaryRequest diaryRequest) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId).orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));

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
            key = "T(String).valueOf(#memberId).concat(':').concat(#date)",
            unless = "#result == null or #result.isEmpty()"
    )
    public List<DiaryResponse> getDailySummariesByMonth(String date, Long memberId) {
        int year=dateFormatutil.yearFormat(date);
        int month=dateFormatutil.monthFormat(date);

        return dailySummaryRepository.findByMonth(year, month, memberId)
                .stream()
                .map(diary -> {
                    float angry = Optional.ofNullable(diary.getAngry()).orElse(0.0f);
                    float sad = Optional.ofNullable(diary.getSad()).orElse(0.0f);
                    float sleepy = Optional.ofNullable(diary.getSleepy()).orElse(0.0f);
                    float excellent = Optional.ofNullable(diary.getExcellent()).orElse(0.0f);
                    float happy = Optional.ofNullable(diary.getHappy()).orElse(0.0f);

                    float total = angry + sad + sleepy + excellent + happy;

                    int angryRatio = total == 0 ? 0 : Math.round((angry / total) * 100f);
                    int sadRatio = total == 0 ? 0 : Math.round((sad / total) * 100f);
                    int sleepyRatio = total == 0 ? 0 : Math.round((sleepy / total) * 100f);
                    int excellentRatio = total == 0 ? 0 : Math.round((excellent / total) * 100f);
                    int happyRatio = total == 0 ? 0 : Math.round((happy / total) * 100f);

                    return new DiaryResponse(
                            Feeling.toValue(diary.getFeeling().toString()),
                            diary.getDiary(),
                            diary.getDate(),
                            diary.getSummary(),
                            angryRatio,
                            sadRatio,
                            sleepyRatio,
                            excellentRatio,
                            happyRatio
                    );
                })
                .toList();

    }
}