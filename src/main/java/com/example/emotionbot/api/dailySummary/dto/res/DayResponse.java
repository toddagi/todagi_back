package com.example.emotionbot.api.dailySummary.dto.res;

import com.example.emotionbot.api.dailySummary.entity.Feeling;

import java.time.LocalDate;
import java.util.List;

public record DayResponse(List<WeeklyFeeling> weeklyFeelingList,
                          float angry, float annoy, float sleepy, float good, float happy) {
    public record WeeklyFeeling(LocalDate date, Feeling feeling) {}
}
