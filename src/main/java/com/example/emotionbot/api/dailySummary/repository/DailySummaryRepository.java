package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.entity.DailySummary;
import com.example.emotionbot.api.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {

    @Query("SELECT ds FROM DailySummary ds WHERE MONTH(ds.date) = :month AND YEAR(ds.date)=:year AND ds.member.id = :memberId")
    List<DailySummary> findByMonth(int year, int month, Long memberId);


    @Query("""
                SELECT 
                    (COUNT(ds) * 1.0) / :daysInMonth *100
                FROM DailySummary ds
                WHERE ds.member.id = :memberId
                  AND YEAR(ds.date) = :year
                  AND MONTH(ds.date) = :month
            """)
    Float getDiaryAverage(@Param("memberId") Long memberId,
                          @Param("year") int year,
                          @Param("month") int month,
                          @Param("daysInMonth") int daysInMonth
    );


    boolean existsByMemberAndDate(Member member, LocalDate today);
}
