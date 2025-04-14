package com.example.emotionbot.api.dailySummary.service;

import com.example.emotionbot.api.dailySummary.dto.req.DiaryRequest;
import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.dailySummary.repository.DailySummaryRepository;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailySummaryService {
    private final DailySummaryRepository dailySummaryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveOrUpdateDiary(Long memberId, DiaryRequest diaryRequest) {
        Member member=memberRepository.findById(memberId).orElseThrow(()->new RuntimeException("Member not found"));
        Optional<DailySummary> existingSummary=dailySummaryRepository.findByMemberAndDate(member, diaryRequest.date());

        if (existingSummary.isPresent()) {
            // 기존 데이터가 있으면 업데이트
            DailySummary dailySummary = existingSummary.get();
            dailySummary.updateDiary(diaryRequest.diary());
            return dailySummaryRepository.save(dailySummary).getId();
        } else {
            // 없으면 새로 저장
            DailySummary newDailySummary = DailySummary.builder()
                    .member(member)
                    .date(diaryRequest.date())
                    .diary(diaryRequest.diary())
                    .build();
            return dailySummaryRepository.save(newDailySummary).getId();
        }
    }

}
