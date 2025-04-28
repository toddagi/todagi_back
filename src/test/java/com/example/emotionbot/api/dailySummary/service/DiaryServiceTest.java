package com.example.emotionbot.api.dailySummary.service;

import com.example.emotionbot.api.dailySummary.dto.res.DiaryResponse;
import com.example.emotionbot.api.dailySummary.repository.DailySummaryRepository;
import com.example.emotionbot.api.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DiaryServiceTest {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DailySummaryRepository dailySummaryRepository;

   @Test
    void 일기조회__캐시_작동을_확인한다(){
       int year = 2025;
       int month = 4;
       Long memberId = 1L;

       List<DiaryResponse> firstCallResult = diaryService.getDailySummariesByMonth(year, month, memberId);

       assertThat(firstCallResult).isNotNull();

       Cache diarySummaryCache = cacheManager.getCache("diarySummary");
       assertNotNull(diarySummaryCache);

       String cacheKey = String.valueOf(memberId) + ":" + year + "-" + month;
       assertNotNull(diarySummaryCache.get(cacheKey));
   }

}