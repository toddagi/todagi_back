package com.example.emotionbot.api.dailySummary.rest;

import java.util.List;
import com.example.emotionbot.api.dailySummary.dto.req.DiaryRequest;
import com.example.emotionbot.api.dailySummary.dto.res.DiaryResponse;
import com.example.emotionbot.api.dailySummary.service.DailySummaryService;
import com.example.emotionbot.common.resolver.MemberId;
import com.example.emotionbot.common.response.APISuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@Slf4j
public class DailySummaryController {
    private final DailySummaryService dailySummaryService;

    @Operation(summary = "일기 작성", description = "일기를 작성합니다")
    @PostMapping("/create")
    public ResponseEntity<APISuccessResponse<Long>> createDiary(@MemberId Long memberId, @RequestBody DiaryRequest diaryRequest){
        return ResponseEntity.ok(APISuccessResponse.ofCreateSuccess(dailySummaryService.saveDiary(memberId, diaryRequest)));
    }

    @GetMapping
    public ResponseEntity<APISuccessResponse<List<DiaryResponse>>> getDailySummariesByMonth(@MemberId Long memberId,@RequestParam int year, @RequestParam int month) {
        List<DiaryResponse> dailySummaries = dailySummaryService.getDailySummariesByMonth(year,month,memberId);
        if (dailySummaries.isEmpty()) {
            return ResponseEntity.noContent().build(); // 일기가 없을 경우 204 반환
        }
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(dailySummaries));
    }


}
