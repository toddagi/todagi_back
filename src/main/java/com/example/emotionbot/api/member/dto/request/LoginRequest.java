package com.example.emotionbot.api.member.dto.request;

import com.example.emotionbot.api.member.dto.response.LoginResponse;
import lombok.Builder;

@Builder
public record LoginRequest(
        String loginId,
        String password
){

}
