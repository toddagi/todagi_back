package com.example.emotionbot.api.dailySummary.repository.impl;

import com.example.emotionbot.api.dailySummary.dto.res.DayResponse;
import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import com.example.emotionbot.api.dailySummary.entity.QDailySummary;
import com.example.emotionbot.api.dailySummary.repository.SummaryRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SummaryRepositoryImpl implements SummaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MonthResponse.AverageFeeling getAverageFeeling(Long memberId, int year, int month) {
        QDailySummary ds = QDailySummary.dailySummary;

     Tuple result=queryFactory.select(
                        ds.angry.avg(),
                        ds.annoy.avg(),
                        ds.sleepy.avg(),
                        ds.good.avg(),
                        ds.happy.avg()
                )
                .from(ds)
                .where(
                        ds.member.id.eq(memberId),
                        ds.date.year().eq(year),
                        ds.date.month().eq(month)
                )
                .fetchOne();
        return new MonthResponse.AverageFeeling(
                result.get(ds.angry.avg()),
                result.get(ds.annoy.avg()),
                result.get(ds.sleepy.avg()),
                result.get(ds.good.avg()),
                result.get(ds.happy.avg())
        );
    }

    @Override
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
                .fetch();
    }

    @Override
    public List<DayResponse.WeeklyFeeling> getWeeklyFeeling(Long memberId, LocalDate date) {
        QDailySummary ds = QDailySummary.dailySummary;

        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return queryFactory
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
                .orderBy(ds.date.asc())
                .fetch();
    }
}


