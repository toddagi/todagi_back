package com.example.emotionbot.api.member.rest;

import com.example.emotionbot.common.auth.BearerAuthExtractor;
import com.example.emotionbot.api.jwt.service.TokenService;
import com.example.emotionbot.api.member.dto.request.LoginRequest;
import com.example.emotionbot.api.member.dto.request.SignUpRequest;
import com.example.emotionbot.api.member.dto.response.LoginResponse;
import com.example.emotionbot.api.member.dto.response.RefreshResponse;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.service.MemberService;
import com.example.emotionbot.common.resolver.MemberAuth;
import com.example.emotionbot.common.resolver.Authentication;
import com.example.emotionbot.common.response.APISuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final TokenService tokenService;
    private final BearerAuthExtractor bearerAuthExtractor;

    @PostMapping("/sign-up")
    public ResponseEntity<APISuccessResponse<Long>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long createdMemberId = memberService.createAccount(signUpRequest);
        return APISuccessResponse.of(HttpStatus.OK, createdMemberId);
    }

    @PostMapping("/login")
    public ResponseEntity<APISuccessResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        final LoginResponse loginResponse = memberService.login(loginRequest);
        return APISuccessResponse.of(HttpStatus.OK, loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<APISuccessResponse<RefreshResponse>> refresh(
            @RequestHeader("Authorization") String bearerToken
    ) {
        final String refreshToken = bearerAuthExtractor.extractTokenValue(bearerToken);
        final String newAccessToken = memberService.refreshAccessToken(refreshToken);

        RefreshResponse refreshResponse = RefreshResponse.of(newAccessToken);
        return APISuccessResponse.of(HttpStatus.OK, refreshResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<APISuccessResponse<Void>> logout(@Authentication MemberAuth memberAuth) {
        final Long memberId = memberAuth.memeberId();
        memberService.logOut(memberId);
        return APISuccessResponse.of(HttpStatus.OK, null);
    }

}
