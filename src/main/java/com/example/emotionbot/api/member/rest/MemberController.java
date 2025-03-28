package com.example.emotionbot.api.member.rest;

import com.example.emotionbot.api.member.dto.req.LoginReqDto;
import com.example.emotionbot.api.member.dto.req.SignUpReqDto;
import com.example.emotionbot.api.member.service.MemberService;
import com.example.emotionbot.common.response.APISuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<APISuccessResponse<Long>> signUp(@Valid @RequestBody SignUpReqDto signUpReqDto){
        Long createdMemberId = memberService.createAccount(signUpReqDto);
        return APISuccessResponse.of(HttpStatus.OK, createdMemberId);
    }

    @PostMapping("/login")
    public ResponseEntity<APISuccessResponse<String>> login(@Valid @RequestBody LoginReqDto loginReqDto){
        return APISuccessResponse.of(HttpStatus.OK,memberService.login(loginReqDto));
    }

}
