package com.example.emotionbot.api.dailySummary.rest;

import com.example.emotionbot.api.dailySummary.dto.res.DayResponse;
import com.example.emotionbot.api.dailySummary.dto.res.MonthResponse;
import com.example.emotionbot.api.dailySummary.service.SummaryService;
import com.example.emotionbot.common.resolver.MemberId;
import com.example.emotionbot.common.response.APISuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/summary")
public class SummaryController {
    private final SummaryService summaryService;

    @Operation(summary = "달 별 통계 조회", description = "달 별로 통계를 조회합니다")
    @GetMapping("/month")
    public ResponseEntity<APISuccessResponse<MonthResponse>> getMonthSummary(@MemberId Long memberId, @RequestParam String date) {
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(summaryService.getMonthSummary(memberId, date)));
    }

    @Operation(summary = "일 별 통계 조회", description = "일 별로 통계를 조회합니다")
    @GetMapping("/day")
    public ResponseEntity<APISuccessResponse<DayResponse>> getDaySummary(@MemberId Long memberId, @RequestParam String date) {
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(summaryService.getDaySummary(memberId, date)));
    }


}
