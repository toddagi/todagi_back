package com.example.emotionbot.api.dailySummary.rest;

import com.example.emotionbot.api.dailySummary.dto.req.DiaryRequestDto;
import com.example.emotionbot.api.dailySummary.service.DailySummaryService;
import com.example.emotionbot.common.resolver.MemberId;
import com.example.emotionbot.common.response.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DailySummaryController {
    private final DailySummaryService dailySummaryService;
    @PostMapping
    public ResponseEntity<APISuccessResponse<Long>> createDiary(@MemberId Long memberId, @RequestBody DiaryRequestDto diaryRequestDto){
        Long diaryId=dailySummaryService.saveOrUpdateDiary(memberId,diaryRequestDto);
        return APISuccessResponse.of(HttpStatus.OK, diaryId);

    }

}
