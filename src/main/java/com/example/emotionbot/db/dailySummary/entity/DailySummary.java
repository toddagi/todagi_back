package com.example.emotionbot.db.dailySummary.entity;

import com.example.emotionbot.db.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_summary")
public class DailySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", columnDefinition = "bigint", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //Todo: LocalDate로 할 지 추후 논의 필요, nullable 여부도 논의 필요
    @Column(name = "date", columnDefinition = "timestamp", nullable = false)
    private LocalDateTime date;

    @Column(name = "summary", columnDefinition = "varchar(100)", nullable = true)
    private String summary;

    @Column(name = "diary", columnDefinition = "varchar(100)", nullable = true)
    private String diary;
}
