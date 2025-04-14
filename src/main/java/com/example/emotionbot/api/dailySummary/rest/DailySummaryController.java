package com.example.emotionbot.api.dailySummary.rest;

import com.example.emotionbot.api.dailySummary.dto.req.DiaryRequest;
import com.example.emotionbot.api.dailySummary.service.DailySummaryService;
import com.example.emotionbot.common.resolver.MemberId;
import com.example.emotionbot.common.response.APISuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "일기 작성", description = "일기를 작성합니다")
    @PostMapping
    public ResponseEntity<APISuccessResponse<Long>> createDiary(@MemberId Long memberId, @RequestBody DiaryRequest diaryRequest){
        return ResponseEntity.ok(APISuccessResponse.ofCreateSuccess(dailySummaryService.saveOrUpdateDiary(memberId, diaryRequest)));
    }

}
