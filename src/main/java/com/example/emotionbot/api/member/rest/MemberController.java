package com.example.emotionbot.api.member.rest;

import com.example.emotionbot.api.member.dto.response.MemberInformationResponse;
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
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "사용자 정보")
    @GetMapping("/information")
    public ResponseEntity<APISuccessResponse<MemberInformationResponse>> consumeClover(@MemberId Long memberId) {
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(memberService.getMemberInformation(memberId)));
    }

    @Operation(summary = "클로버 소모")
    @PostMapping("/consume-clover")
    public ResponseEntity<APISuccessResponse<Long>> consumeClover(@MemberId Long memberId, @RequestBody ConsumeCloverRequest consumeCloverRequest) {
        memberService.consumeClover(memberId, consumeCloverRequest);
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(null));
    }

    @Operation(summary = "닉네임 설정")
    @PostMapping("/change-nickname")
    public ResponseEntity<APISuccessResponse<Long>> changeNickname(@MemberId Long memberId, @RequestParam String nickName) {
        memberService.changeNickname(memberId,nickName);
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(null));
    }
}
