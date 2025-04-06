package com.example.emotionbot.api.member.service;

import com.example.emotionbot.api.member.dto.request.LoginRequest;
import com.example.emotionbot.api.member.dto.request.SignUpRequest;
import com.example.emotionbot.api.member.dto.response.LoginResponse;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import com.example.emotionbot.common.utils.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public Long createAccount(@Valid SignUpRequest signUpRequest) {

        if (memberRepository.existsByLoginId(signUpRequest.loginId())) {
            throw new EmotionBotException(FailMessage.CONFLICT_DUPLICATE_ID);
        }
        Member member = Member.builder()
                .loginId(signUpRequest.loginId())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .nickname(signUpRequest.nickname())
                .clover(0)
                .talkType(signUpRequest.talkType())
                .keyboardYn(signUpRequest.keyboardYn())
                .build();
        return memberRepository.save(member).getId();
    }

    public LoginResponse login(@Valid LoginRequest loginRequest) {
        Member member = memberRepository.findByLoginId(loginRequest.loginId()).orElseThrow(() ->
                new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new EmotionBotException(FailMessage.CONFLICT_WRONG_PW);
        }

        String accessToken=jwtTokenUtil.createToken(loginRequest.loginId());
        String refreshToken=jwtTokenUtil.createRefreshToken(loginRequest.loginId());

        redisTemplate.opsForValue().set(
                    member.getLoginId(),
                    refreshToken,
                    jwtTokenUtil.getRefreshTokenExpirationMillis(),
                    TimeUnit.MILLISECONDS
        );
        return LoginResponse.of(accessToken,refreshToken);
    }
}
