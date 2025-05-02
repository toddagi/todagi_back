package com.example.emotionbot.api.dailySummary.dto.res;

import com.example.emotionbot.api.dailySummary.entity.Feeling;

import java.time.LocalDate;
import java.util.List;

public record DayResponse(List<WeeklyFeeling> weeklyFeelingList,
                          EmotionScores emotionScores
) {
    public record WeeklyFeeling(LocalDate date, Feeling feeling) {
    }

    public record EmotionScores(float angry, float annoy, float sleepy, float good, float happy) {
    }
}
