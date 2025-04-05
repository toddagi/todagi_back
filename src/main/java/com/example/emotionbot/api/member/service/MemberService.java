package com.example.emotionbot.api.member.service;

import com.example.emotionbot.api.jwt.service.TokenService;
import com.example.emotionbot.api.member.dto.request.LoginRequest;
import com.example.emotionbot.api.member.dto.request.SignUpRequest;
import com.example.emotionbot.api.member.dto.response.LoginResponse;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import com.example.emotionbot.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Long createAccount(SignUpRequest signUpReqDto) {

        if (memberRepository.existsByLoginId(signUpReqDto.loginId())) {
            throw new EmotionBotException(FailMessage.CONFLICT_DUPLICATE_ID);
        }
        Member member = Member.builder()
                .loginId(signUpReqDto.loginId())
                .password(passwordEncoder.encode(signUpReqDto.password()))
                .nickname(signUpReqDto.nickname())
                .clover(0)
                .chatMode(signUpReqDto.chatMode())
                .keyboardYn(signUpReqDto.keyboardYn())
                .build();
        return memberRepository.save(member).getId();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByLoginId(loginRequest.loginId()).orElseThrow(() ->
                new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new EmotionBotException(FailMessage.CONFLICT_WRONG_PW);
        }
        return tokenService.generateToken(member.getId());
    }


    public String refreshAccessToken(String refreshToken) {
        final Long userId = jwtUtil.getMemberIdFromToken(refreshToken);

        String storedRefreshToken = tokenService.getRefreshToken(userId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new EmotionBotException(FailMessage.UNAUTHORIZED);
        }

        return jwtUtil.generateAccessToken(userId);
    }

    @Transactional
    public void logOut(final Long userId) {
        tokenService.deleteRefreshToken(userId);
    }
}
