package com.example.emotionbot.api.dailySummary.dto.res;

import com.example.emotionbot.api.dailySummary.entity.Feeling;

import java.time.LocalDate;
import java.util.List;

public record MonthResponse(AverageFeeling averageFeelings,
                            List<DailyFeeling> dailyFeelings,
                            Float diaryAvg) {
    public record AverageFeeling(
            double angry,
            double sad,
            double sleepy,
            double excellent,
            double happy
    ) {
    }

    public record DailyFeeling(
            LocalDate date,
            Feeling feeling
    ) {
    }
}
