package com.example.emotionbot.db.monthSummary.entity;

import com.example.emotionbot.db.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "month_summary")
public class MonthSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", columnDefinition = "bigint", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "date", columnDefinition = "timestamp", nullable = false)
    private YearMonth date;

    @Column(name = "summary", columnDefinition = "varchar(100)", nullable = false)
    private String summary;
}
