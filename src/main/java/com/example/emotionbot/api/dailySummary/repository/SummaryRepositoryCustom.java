package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryRepositoryCustom {
    MonthResponse.AverageFeeling getAverageFeeling(Long memberId, int year, int month);

}
