package com.example.emotionbot.api.dailySummary.repository.impl;

import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import com.example.emotionbot.api.dailySummary.entity.QDailySummary;
import com.example.emotionbot.api.dailySummary.repository.SummaryRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
