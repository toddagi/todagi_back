package com.example.emotionbot.api.dailySummary.service;

import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.dailySummary.repository.DailySummaryRepository;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyScheduler {

    private final MemberRepository memberRepository;
    private final DailySummaryRepository dailySummaryRepository;

    @Scheduled(cron="0 0 4 * * *")
    @Transactional
    public void dailySchedule() {
        List<Member> members = memberRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Member member : members) {
            boolean exists = dailySummaryRepository.existsByMemberAndDate(member, today);
            if (!exists) {
                DailySummary summary= DailySummary.builder().member(member).date(today).build();
                dailySummaryRepository.save(summary);
            }
        }
    }
}
