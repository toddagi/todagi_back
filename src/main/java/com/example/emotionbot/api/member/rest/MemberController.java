package com.example.emotionbot.api.member.rest;

import com.example.emotionbot.api.member.dto.request.LoginRequest;
import com.example.emotionbot.api.member.dto.request.SignUpRequest;
import com.example.emotionbot.api.member.dto.response.LoginResponse;
import com.example.emotionbot.api.member.service.MemberService;
import com.example.emotionbot.common.response.APISuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/sign-up")
    public APISuccessResponse<Long> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long createdMemberId = memberService.createAccount(signUpRequest);
        return APISuccessResponse.ofCreateSuccess(createdMemberId);
    }

    @PostMapping("/login")
    public APISuccessResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return APISuccessResponse.ofCreateSuccess(memberService.login(loginRequest));
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token을 통해 Access Token을 재발급받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<APISuccessResponse<LoginResponse>> reissueToken(@RequestHeader("Authorization") String refreshToken){
        return ResponseEntity.ok(APISuccessResponse.ofSuccess(memberService.reissueToken(refreshToken)));
    }
}
