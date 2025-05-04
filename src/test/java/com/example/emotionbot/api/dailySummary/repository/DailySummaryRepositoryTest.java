package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.dailySummary.entity.Feeling;
import com.example.emotionbot.api.member.entity.KeyboardYn;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.entity.TalkType;
import com.example.emotionbot.api.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DailySummaryRepositoryTest {

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DailySummaryRepository dailySummaryRepository;

    private int year;
    private int month;
    private int day;
    private LocalDate date;

    Member member;
    @BeforeEach
    void setUp() {
        year = 2025;
        month = 12;
        day = 04;
        date = LocalDate.of(year, month, day);

        member = Member.builder()
                .loginId("testId")
                .password("testPassword")
                .nickname("testNickname")
                .clover(100)
                .keyboardYn(KeyboardYn.Y)
                .talkType(TalkType.T)
                .build();

        memberRepository.save(member);

        for (int day = 1; day <= 7; day++) {
            DailySummary dailySummary = DailySummary.builder()
                    .member(member)
                    .date(LocalDate.of(year, month, day))
                    .summary("요약 " + day)
                    .diary("일기 내용 " + day)
                    .feeling(Feeling.HAPPY)
                    .build();

            dailySummaryRepository.save(dailySummary);
        }
    }

    @Test
    void 멤버조회(){
       assertThat(member.getLoginId()).isEqualTo("testId");
    }

    @Test
    void getMonthFeelingAverage_정상조회() {
      MonthResponse.AverageFeeling averageFeeling=summaryRepository.getAverageFeeling(member.getId(),year,month);
      assertThat(averageFeeling.angry()).isEqualTo(0.0);
    }

    @Test
    void 날짜별_감정을_반환한다(){
        assertThat(summaryRepository.getDailyFeeling(member.getId(),year,month)).isNotNull();
    }


    @Test
    void 주차별_감정을_반환한다(){
        assertThat(summaryRepository.getWeeklyFeeling(member.getId(),date)).size().isEqualTo(7);
    }

    @Test
    void 하루_다섯개_감정을_반환한다(){
        assertThat(summaryRepository.getEmotionScores(member.getId(),date).angry()).isEqualTo(0.0f);
    }

}