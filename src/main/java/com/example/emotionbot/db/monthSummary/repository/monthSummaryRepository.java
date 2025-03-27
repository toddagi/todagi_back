package com.example.emotionbot.db.monthSummary.repository;

import com.example.emotionbot.db.monthSummary.entity.MonthSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface monthSummaryRepository extends JpaRepository<MonthSummary, Long> {
}
