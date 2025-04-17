package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
    @Query("SELECT ds FROM DailySummary ds WHERE MONTH(ds.date) = :month AND YEAR(ds.date)=:year AND ds.member.id = :memberId")
    List<DailySummary> findByMonth(int year,int month,Long memberId);
}
