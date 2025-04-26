package com.example.emotionbot.api.challenge.repository;

import com.example.emotionbot.api.challenge.entity.Challenge;
import com.example.emotionbot.api.challenge.entity.ChallengeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByMemberIdAndChallengeOption(Long memberId, ChallengeOption challengeOption);
}
