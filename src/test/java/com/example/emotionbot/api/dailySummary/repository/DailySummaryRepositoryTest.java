package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DailySummaryRepositoryTest {

    @Autowired
    private DailySummaryRepository dailySummaryRepository;

    private Long memberId;
    private int year;
    private int month;
    private int day;

    @BeforeEach
    void setUp() {
        memberId = 2L;
        year = 2025;
        month = 12;
        day = 30;
    }

    @Test
    void getMonthFeelingAverage_정상조회() {
        Float[] averages = dailySummaryRepository.getMonthFeelingAverage(memberId, year, month);
        assertThat(averages[0]).isNotNull();
    }

    @Test
    void getDiaryAverage_정상조회() {
        Float result = dailySummaryRepository.getDiaryAverage(memberId, year, month, day);
        assertEquals(result,100);
    }

}