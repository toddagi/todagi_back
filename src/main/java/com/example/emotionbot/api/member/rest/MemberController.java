package com.example.emotionbot.api.member.rest;

import com.example.emotionbot.api.member.dto.request.*;
import com.example.emotionbot.api.member.dto.response.MemberInformationResponse;
import com.example.emotionbot.api.member.service.MemberService;
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

    @Operation(summary = "닉네임 변경")
    @PostMapping("/change/nickname")
    public ResponseEntity<APISuccessResponse<Long>> changeNickname(@MemberId Long memberId, @RequestBody NickNameRequest nickNameRequest) {
        memberService.changeNickname(memberId, nickNameRequest.nickName());
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(null));
    }

    @Operation(summary = "talkType 변경")
    @PostMapping("/change/talk-type")
    public ResponseEntity<APISuccessResponse<Long>> changeNickname(@MemberId Long memberId, @RequestBody TalkTypeValueRequest talkTypeValueRequest) {
        memberService.changeTalkType(memberId, talkTypeValueRequest.talkTypeValue());
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(null));
    }

    @Operation(summary = "키보드 yn 변경")
    @PostMapping("/change/keyboard-yn")
    public ResponseEntity<APISuccessResponse<Long>> changeKeyBoardYn(@MemberId Long memberId, @RequestBody KeyBoardYnRequest keyBoardYnRequest) {
        memberService.changeKeyBoardYn(memberId, keyBoardYnRequest.keyBoardYn());
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(null));
    }

    @Operation(summary = "push yn 변경")
    @PostMapping("/change/push-yn")
    public ResponseEntity<APISuccessResponse<Long>> changeKeyBoardYn(@MemberId Long memberId, @RequestBody PushYnRequest pushYnRequest) {
        memberService.changeKeyBoardYn(memberId, pushYnRequest.pushYn());
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(null));
    }
}
