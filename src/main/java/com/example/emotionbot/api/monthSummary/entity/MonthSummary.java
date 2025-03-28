package com.example.emotionbot.api.monthSummary.entity;

import com.example.emotionbot.api.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @JoinColumn(name = "member_id", columnDefinition = "bigint(20)", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "date", columnDefinition = "timestamp", nullable = false)
    private YearMonth date;

    @Column(name = "summary", columnDefinition = "varchar(100)", nullable = true)
    private String summary;

    @Builder
    public MonthSummary(Long id, Member member, YearMonth date, String summary) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.summary = summary;
    }
}
