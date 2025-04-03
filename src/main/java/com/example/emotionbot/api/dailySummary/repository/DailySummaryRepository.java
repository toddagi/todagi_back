package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
    Optional<DailySummary> findByMemberAndDate(Member member, LocalDate date);
}
