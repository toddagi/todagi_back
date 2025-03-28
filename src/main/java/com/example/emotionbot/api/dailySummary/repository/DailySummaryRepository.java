package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
}
