package com.example.emotionbot.api.dailySummary.rest;

import com.example.emotionbot.api.dailySummary.dto.req.DiaryRequest;
import com.example.emotionbot.api.dailySummary.dto.res.DiaryResponse;
import com.example.emotionbot.api.dailySummary.service.DiaryService;
import com.example.emotionbot.common.resolver.MemberId;
import com.example.emotionbot.common.response.APISuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@Slf4j
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(summary = "일기 작성", description = "일기를 작성합니다")
    @PostMapping("/create")
    public ResponseEntity<APISuccessResponse<Long>> createDiary(@MemberId Long memberId, @RequestBody DiaryRequest diaryRequest) {
        return ResponseEntity.ok(APISuccessResponse.ofCreateSuccess(diaryService.saveDiary(memberId, diaryRequest)));
    }

    @Operation(summary = "요일 별 일기 조회", description = "월별로 일기를 조회합니다")
    @GetMapping
    public ResponseEntity<APISuccessResponse<List<DiaryResponse>>> getDailySummariesByMonth(@MemberId Long memberId, @RequestParam String date) {
        List<DiaryResponse> dailySummaries = diaryService.getDailySummariesByMonth(date, memberId);
        if (dailySummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(dailySummaries));
    }
}
