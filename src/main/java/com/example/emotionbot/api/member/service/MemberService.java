package com.example.emotionbot.api.member.service;

import com.example.emotionbot.api.member.dto.req.LoginReqDto;
import com.example.emotionbot.api.member.dto.req.SignUpReqDto;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import com.example.emotionbot.common.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     회원가입
     */
    public Long createAccount(SignUpReqDto signUpReqDto) {

        if(memberRepository.existsByLoginId(signUpReqDto.getLoginId())){
            throw new EmotionBotException(FailMessage.CONFLICT_DUPLICATE_ID);
        }
        Member member= Member.builder()
                .loginId(signUpReqDto.getLoginId())
                .password(passwordEncoder.encode(signUpReqDto.getPassword()))
                .nickname(signUpReqDto.getNickname())
                .clover(0)
                .chatMode(signUpReqDto.getChatMode())
                .keyboardYn(signUpReqDto.getKeyboardYn())
                .build();
        return memberRepository.save(member).getId();
    }

    /**
     * 로그인
     */
    public String login(LoginReqDto loginReqDto) {
        Member member=memberRepository.findByLoginId(loginReqDto.getLoginId()).orElseThrow(()->
                new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        if (!passwordEncoder.matches(loginReqDto.getPassword(), member.getPassword())){
            throw new EmotionBotException(FailMessage.CONFLICT_WRONG_PW);
        }
        return jwtTokenUtil.createToken(member.getLoginId());
    }
}
