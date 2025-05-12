package com.example.emotionbot.api.dailySummary.entity;

import com.example.emotionbot.api.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_summary")
public class DailySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", columnDefinition = "bigint(20)", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "date", columnDefinition = "date", nullable = false)
    private LocalDate date;

    @Column(name = "summary", columnDefinition = "varchar(100)", nullable = true)
    private String summary;

    @Column(name = "diary", columnDefinition = "varchar(100)", nullable = true)
    private String diary;

    @Column(name = "feeling")
    @Enumerated(value = EnumType.STRING)
    private Feeling feeling;

    @Column(name = "angry", columnDefinition = "double")
    private float angry;

    @Column(name = "sad", columnDefinition = "double")
    private float sad;

    @Column(name = "sleepy", columnDefinition = "double")
    private float sleepy;

    @Column(name = "excellent", columnDefinition = "double")
    private float excellent;

    @Column(name = "happy", columnDefinition = "double")
    private float happy;


    @Builder
    public DailySummary(Long id, Member member, LocalDate date, String summary, String diary, Feeling feeling) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.summary = summary;
        this.diary = diary;
        this.feeling = feeling;
    }

    public void updateDiary(String diary) {
        this.diary = diary;
    }
}
