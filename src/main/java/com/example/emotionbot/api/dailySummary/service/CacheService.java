package com.example.emotionbot.api.dailySummary.service;

import com.example.emotionbot.api.dailySummary.dto.res.DiaryResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    private final  Cache<String, List<DiaryResponse>> cache;

    public CacheService() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();
    }

    private String buildKey(int year, int month, Long memberId) {
        return year + "-" + month + "-" + memberId;
    }

    public List<DiaryResponse> get(int year, int month, Long memberId) {
        return cache.getIfPresent(buildKey(year, month, memberId));
    }

    public void put(int year, int month, Long memberId, List<DiaryResponse> data) {
        cache.put(buildKey(year, month, memberId), data);
    }

    public boolean contains(int year, int month, Long memberId) {
        return cache.getIfPresent(buildKey(year, month, memberId)) != null;
    }
}
