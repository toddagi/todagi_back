package com.example.emotionbot.api.dailySummary.service;

import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.dailySummary.repository.DailySummaryRepository;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyScheduler {

    private static final int THREAD_COUNT = 8;

    private final MemberRepository memberRepository;
    private final DailySummaryRepository dailySummaryRepository;
    private final TransactionTemplate transactionTemplate;

    @Scheduled(cron="0 0 4 * * *")
    public void dailySchedule() {
        List<Member> members = memberRepository.findAll();
        LocalDate today = LocalDate.now();
        processMembersInParallel(members, today);
    }

    private void processMembersInParallel(List<Member> members, LocalDate date) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        int chunkSize = (int) Math.ceil((double) members.size() / THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            int fromIndex = i * chunkSize;
            int toIndex = Math.min(fromIndex + chunkSize, members.size());

            if (fromIndex >= toIndex) break;

            List<Member> chunk = members.subList(fromIndex, toIndex);

            executorService.submit(() -> processMemberChunk(chunk, date));
        }

        executorService.shutdown();
    }

    private void processMemberChunk(List<Member> members, LocalDate date) {
        String threadName = Thread.currentThread().getName();
        log.info("스레드 {}에서 {}명의 멤버 처리 시작", threadName, members.size());

        for (Member member : members) {
            try {
                transactionTemplate.executeWithoutResult(status -> {
                    boolean exists = dailySummaryRepository.existsByMemberAndDate(member, date);
                    if (!exists) {
                        DailySummary summary = DailySummary.builder()
                                .member(member)
                                .date(date)
                                .build();
                        dailySummaryRepository.save(summary);
                    }
                });
            } catch (Exception e) {
                log.error("스레드 {} - member id {} 처리 중 오류", threadName, member.getId(), e);
            }
        }

        log.info("스레드 {} 작업 완료", threadName);
    }
}


