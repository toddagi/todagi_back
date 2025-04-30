package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.dto.res.DayResponse;
import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SummaryRepositoryCustom {
    MonthResponse.AverageFeeling getAverageFeeling(Long memberId, int year, int month);
    List<MonthResponse.DailyFeeling> getDailyFeeling(Long memberId, int year, int month);

    List<DayResponse.WeeklyFeeling> getWeeklyFeeling(Long memberId, LocalDate date);


}
