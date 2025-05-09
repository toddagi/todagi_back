package com.example.emotionbot.api.dailySummary.repository;

import com.example.emotionbot.api.dailySummary.entity.Keyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyboardRepository extends JpaRepository<Keyboard, Long> {
}
