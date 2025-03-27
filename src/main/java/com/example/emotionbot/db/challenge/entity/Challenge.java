package com.example.emotionbot.db.challenge.entity;

import com.example.emotionbot.db.member.entity.Member;
import com.example.emotionbot.enums.challenge.ChallengeOption;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "challenge")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", columnDefinition = "bigint", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "challenge_option", columnDefinition = "varchar(20)", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ChallengeOption challengeOption;

    @Column(name = "stamp_count", columnDefinition = "bigint(20)", nullable = false)
    private int stampCount;

    @Builder
    public Challenge(Long id, Member member, ChallengeOption challengeOption, int stampCount) {
        this.id = id;
        this.member = member;
        this.challengeOption = challengeOption;
        this.stampCount = stampCount;
    }
}
