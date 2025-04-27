package com.example.emotionbot.api.challenge.rest;

import com.example.emotionbot.api.challenge.dto.checkChallengeResponse;
import com.example.emotionbot.api.challenge.service.ChallengeService;
import com.example.emotionbot.api.dailySummary.dto.req.DiaryRequest;
import com.example.emotionbot.common.resolver.MemberId;
import com.example.emotionbot.common.response.APISuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    @Operation(summary = "챌린지 체크", description = "챌린지 현황을 체크합니다.")
    @PostMapping("/check")
    public ResponseEntity<APISuccessResponse<List<checkChallengeResponse>>> createDiary(@MemberId Long memberId){
        return ResponseEntity.ok(APISuccessResponse.ofCreateSuccess(challengeService.checkChallenge(memberId)));
    }
}
