package com.example.emotionbot.api.dailySummary.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Keyboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @Column(name = "member_id", columnDefinition = "bigint(20)", nullable = false)
    private Long memberId;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "text", columnDefinition = "text", nullable = true)
    private String text;

    @Column(name="fixed_text",columnDefinition = "text",nullable = true)
    private String fixed_text;

    @Builder
    public Keyboard(Long memberId, String text) {
        this.memberId=memberId;
        this.date=LocalDateTime.now();
        this.text=text;
    }
}
