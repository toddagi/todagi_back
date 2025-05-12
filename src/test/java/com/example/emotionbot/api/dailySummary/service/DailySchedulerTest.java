package com.example.emotionbot.api.dailySummary.service;

import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.dailySummary.repository.DailySummaryRepository;
import com.example.emotionbot.api.member.entity.KeyboardYn;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.entity.TalkType;
import com.example.emotionbot.api.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DailySchedulerTest {

    @Test
    @DisplayName("dailySchedule 메서드는 매일 오전 4시에 실행되어야 한다")
    void shouldTrigger_dailySchedule_at4AMDaily() throws ParseException {
        String cronExpression = "0 0 4 * * *";
        CronTrigger trigger = new CronTrigger(cronExpression);

        Date startTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2025/12/20 00:00:00");
        SimpleTriggerContext context = new SimpleTriggerContext();
        context.update(startTime, startTime, startTime);

        List<String> expectedTimes = Arrays.asList(
                "2025/12/20 04:00:00",
                "2025/12/21 04:00:00",
                "2025/12/22 04:00:00"
        );

        for (String expectedTime : expectedTimes) {
            Date nextExecutionTime = trigger.nextExecutionTime(context);
            String actualTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(nextExecutionTime);
            assertThat(actualTime).isEqualTo(expectedTime);
            context.update(nextExecutionTime, nextExecutionTime, nextExecutionTime);
        }
    }

    @Test
    @DisplayName("하루에 한번 회원수만큼 행이 추가된다")
    void make_data_by_member(){
        MemberRepository memberRepository = mock(MemberRepository.class);
        DailySummaryRepository dailySummaryRepository = mock(DailySummaryRepository.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        // 2. 트랜잭션 실행 시 그냥 람다 실행하도록 설정
        when(transactionManager.getTransaction(any())).thenReturn(mock(TransactionStatus.class));

        List<Member> members=new ArrayList<>();
        for (int i=0;i<20000;i++){
            Member member=Member.builder()
                    .loginId("testId")
                    .password("testPassword")
                    .nickname("testNickname")
                    .clover(100)
                    .keyboardYn(KeyboardYn.Y)
                    .talkType(TalkType.T)
                    .build();
            members.add(member);
        }

        when(memberRepository.findAll()).thenReturn(members);
        when(dailySummaryRepository.existsByMemberAndDate(any(), any())).thenReturn(false);
        when(dailySummaryRepository.save(any(DailySummary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DailyScheduler scheduler = new DailyScheduler(memberRepository, dailySummaryRepository,transactionTemplate);

        long start = System.currentTimeMillis();
        scheduler.dailySchedule();
        long end = System.currentTimeMillis();

        System.out.println("Execution time with mock: " + ((end - start) / 1000.0) + " seconds");
    }



}