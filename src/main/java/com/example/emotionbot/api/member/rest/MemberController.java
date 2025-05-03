package com.example.emotionbot.api.member.rest;

import com.example.emotionbot.api.member.service.MemberService;
import com.example.emotionbot.api.member.dto.request.ConsumeCloverRequest;
import com.example.emotionbot.common.resolver.MemberId;
import com.example.emotionbot.common.response.APISuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "클로버 소모")
    @PostMapping("/consume-clover")
    public ResponseEntity<APISuccessResponse<Long>> consumeClover(@MemberId Long memberId, @RequestBody ConsumeCloverRequest consumeCloverRequest) {
        memberService.consumeClover(memberId, consumeCloverRequest);
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(null));
    }
}
