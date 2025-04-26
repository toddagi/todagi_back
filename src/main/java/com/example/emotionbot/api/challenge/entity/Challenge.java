package com.example.emotionbot.api.challenge.entity;

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
@Table(name = "challenge")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", columnDefinition = "bigint(20)", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "date", columnDefinition = "date", nullable = false)
    private LocalDate date;

    @Column(name = "challenge_option", columnDefinition = "varchar(20)", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ChallengeOption challengeOption;

    @Column(name = "challenge_status", columnDefinition = "bigint(20)", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ChallengeStatus challengeStatus;

    @Builder
    public Challenge(Long id, Member member, ChallengeOption challengeOption, ChallengeStatus challengeStatus) {
        this.id = id;
        this.member = member;
        this.challengeOption = challengeOption;
        this.challengeStatus = challengeStatus;
    }
}
