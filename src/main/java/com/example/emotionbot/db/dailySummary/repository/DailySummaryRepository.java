package com.example.emotionbot.db.dailySummary.repository;

import com.example.emotionbot.db.dailySummary.entity.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
}
