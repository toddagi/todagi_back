package com.example.emotionbot.api.dailySummary.service;

import com.example.emotionbot.api.dailySummary.dto.res.DayResponse;
import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import com.example.emotionbot.api.dailySummary.repository.DailySummaryRepository;
import com.example.emotionbot.api.dailySummary.repository.SummaryRepository;
import com.example.emotionbot.common.utils.DateFormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final DailySummaryRepository dailySummaryRepository;
    private final SummaryRepository summaryRepository;
    private final DateFormatUtil dateFormatUtil;

    public MonthResponse getMonthSummary(Long memberId, String date) {
        int year=dateFormatUtil.yearFormat(date);
        int month=dateFormatUtil.monthFormat(date);

        MonthResponse.AverageFeeling averageFeeling = getAverageFeeling(memberId, year, month);
        Float diaryAvg = getDiaryAverage(memberId, year, month);
        List<MonthResponse.DailyFeeling> dailyFeelings = getDailyFeelings(memberId, year, month);

        return new MonthResponse(averageFeeling, dailyFeelings, diaryAvg);
    }

    private MonthResponse.AverageFeeling getAverageFeeling(Long memberId, int year, int month) {
       return summaryRepository.getAverageFeeling(memberId, year, month);
    }

    private Float getDiaryAverage(Long memberId, int year, int month) {
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        Float diaryAvg = dailySummaryRepository.getDiaryAverage(memberId, year, month, daysInMonth);

        return (diaryAvg != null) ? diaryAvg : 0f;
    }

    private List<MonthResponse.DailyFeeling> getDailyFeelings(Long memberId, int year, int month) {
        return summaryRepository.getDailyFeeling(memberId, year, month);
    }

    public DayResponse getDaySummary(Long memberId, String date) {
        LocalDate formatDate=LocalDate.parse(date);

        List<DayResponse. WeeklyFeeling> weeklyFeelingList=summaryRepository.getWeeklyFeeling(memberId,formatDate);
        DayResponse. EmotionScores emotionScores=summaryRepository.getEmotionScores(memberId,formatDate);

        return new DayResponse(weeklyFeelingList, emotionScores);
    }


}
