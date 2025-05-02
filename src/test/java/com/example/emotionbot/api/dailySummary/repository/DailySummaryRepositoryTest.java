package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.dailySummary.repository.impl.SummaryRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DailySummaryRepositoryTest {

    @Autowired
    private SummaryRepositoryImpl summaryRepository;

    private Long memberId;
    private int year;
    private int month;
    private int day;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        year = 2025;
        month = 12;
        day = 10;
        date = LocalDate.of(year, month, day);
    }

    @Test
    void getMonthFeelingAverage_정상조회() {
      MonthResponse.AverageFeeling averageFeeling=summaryRepository.getAverageFeeling(memberId,year,month);
        System.out.println(averageFeeling);
    }

    @Test
    void 날짜별_감정을_반환한다(){
        assertThat(summaryRepository.getDailyFeeling(memberId,year,month)).isNotNull();
    }


    @Test
    void 주차별_감정을_반환한다(){
        assertThat(summaryRepository.getWeeklyFeeling(memberId,date)).size().isEqualTo(7);
    }

}