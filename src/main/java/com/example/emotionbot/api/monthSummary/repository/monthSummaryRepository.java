package com.example.emotionbot.api.monthSummary.repository;

import com.example.emotionbot.api.monthSummary.entity.MonthSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface monthSummaryRepository extends JpaRepository<MonthSummary, Long> {
}
