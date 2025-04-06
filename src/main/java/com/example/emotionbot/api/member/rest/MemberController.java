package com.example.emotionbot.api.member.rest;

import com.example.emotionbot.api.member.dto.request.LoginRequest;
import com.example.emotionbot.api.member.dto.request.SignUpRequest;
import com.example.emotionbot.api.member.dto.response.LoginResponse;
import com.example.emotionbot.api.member.dto.response.LogoutResponse;
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

    @Operation(summary = "로그아웃", description = "사용자의 Refresh Token을 무효화하여 로그아웃 처리합니다.\n\n"
            + "📌 요청 헤더에 `Authorization: Bearer {access_token}` 형식으로 전달해야 합니다.\n"
            + "✅ 로그아웃 시 해당 토큰은 더 이상 사용할 수 없게 됩니다.")
    @PostMapping("/logout")
    public ResponseEntity<APISuccessResponse<LogoutResponse>> logout(@RequestHeader("Authorization") String refreshToken){
        return ResponseEntity.ok(APISuccessResponse.ofCreateSuccess(memberService.logOut(refreshToken)));
    }
}
