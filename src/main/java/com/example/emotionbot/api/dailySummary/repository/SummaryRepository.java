package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.dto.res.DayResponse;
import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import com.example.emotionbot.api.dailySummary.entity.Feeling;
import com.example.emotionbot.api.dailySummary.entity.QDailySummary;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SummaryRepository {

    private final JPAQueryFactory queryFactory;

    public MonthResponse.AverageFeeling getAverageFeeling(Long memberId, int year, int month) {
        QDailySummary ds = QDailySummary.dailySummary;

        NumberExpression<Double> avgAngry = ds.angry.avg();
        NumberExpression<Double> avgSad = ds.sad.avg();
        NumberExpression<Double> avgSleepy = ds.sleepy.avg();
        NumberExpression<Double> avgExcellent = ds.excellent.avg();
        NumberExpression<Double> avgHappy = ds.happy.avg();

        Tuple result = queryFactory.select(
                        avgAngry,
                        avgSad,
                        avgSleepy,
                        avgExcellent,
                        avgHappy
                )
                .from(ds)
                .where(
                        ds.member.id.eq(memberId),
                        ds.date.year().eq(year),
                        ds.date.month().eq(month)
                )
                .fetchOne();

        double angry = result.get(avgAngry) != null ? result.get(avgAngry) : 0.0;
        double sad = result.get(avgSad) != null ? result.get(avgSad) : 0.0;
        double sleepy = result.get(avgSleepy) != null ? result.get(avgSleepy) : 0.0;
        double excellent = result.get(avgExcellent) != null ? result.get(avgExcellent) : 0.0;
        double happy = result.get(avgHappy) != null ? result.get(avgHappy) : 0.0;

        double sum = angry + sad + sleepy + excellent + happy;

        if (sum == 0.0) {
            return new MonthResponse.AverageFeeling(0, 0, 0, 0, 0);
        }

        return new MonthResponse.AverageFeeling(
                (int) ((angry / sum) * 100),
                (int) ((sad / sum) * 100),
                (int) ((sleepy / sum) * 100),
                (int) ((excellent / sum) * 100),
                (int) ((happy / sum) * 100)
        );
    }


    public List<MonthResponse.DailyFeeling> getDailyFeeling(Long memberId, int year, int month) {
        QDailySummary ds = QDailySummary.dailySummary;

        return queryFactory
                .select(Projections.constructor(
                        MonthResponse.DailyFeeling.class,
                        ds.date,
                        ds.feeling
                ))
                .from(ds)
                .where(
                        ds.member.id.eq(memberId),
                        ds.date.year().eq(year),
                        ds.date.month().eq(month)
                )
                .orderBy(ds.date.asc())
                .fetch();
    }


    public List<DayResponse.WeeklyFeeling> getWeeklyFeeling(Long memberId, LocalDate date) {
        QDailySummary ds = QDailySummary.dailySummary;

        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<DayResponse.WeeklyFeeling> weeklyFeelings = queryFactory
                .select(Projections.constructor(
                        DayResponse.WeeklyFeeling.class,
                        ds.date,
                        ds.feeling
                ))
                .from(ds)
                .where(
                        ds.member.id.eq(memberId),
                        ds.date.between(startOfWeek, endOfWeek)
                )
                .fetch();

        Map<LocalDate, Feeling> feelingMap = weeklyFeelings.stream()
                .collect(Collectors.toMap(DayResponse.WeeklyFeeling::date, DayResponse.WeeklyFeeling::feeling));

        List<DayResponse.WeeklyFeeling> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);
            Feeling feeling = feelingMap.getOrDefault(day, Feeling.UNKOWN);
            result.add(new DayResponse.WeeklyFeeling(day, feeling));
        }

        return result;
    }


    public DayResponse.EmotionScores getEmotionScores(Long memberId, LocalDate date) {
        QDailySummary ds = QDailySummary.dailySummary;

        DayResponse.EmotionScores raw = queryFactory
                .select(Projections.constructor(
                        DayResponse.EmotionScores.class,
                        ds.angry,
                        ds.sad,
                        ds.sleepy,
                        ds.excellent,
                        ds.happy
                ))
                .from(ds)
                .where(
                        ds.member.id.eq(memberId),
                        ds.date.eq(date)
                )
                .fetchOne();

        float angry = raw != null ? raw.angry() : 0f;
        float sad = raw != null ? raw.sad() : 0f;
        float sleepy = raw != null ? raw.sleepy() : 0f;
        float excellent = raw != null ? raw.excellent() : 0f;
        float happy = raw != null ? raw.happy() : 0f;

        float sum = angry + sad + sleepy + excellent + happy;

        if (sum == 0f) {
            return new DayResponse.EmotionScores(0, 0, 0, 0, 0);
        }

        return new DayResponse.EmotionScores(
                (int) ((angry / sum) * 100),
                (int) ((sad / sum) * 100),
                (int) ((sleepy / sum) * 100),
                (int) ((excellent / sum) * 100),
                (int) ((happy / sum) * 100)
        );
    }


}

