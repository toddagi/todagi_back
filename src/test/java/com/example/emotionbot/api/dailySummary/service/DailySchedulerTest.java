package com.example.emotionbot.api.dailySummary.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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


}