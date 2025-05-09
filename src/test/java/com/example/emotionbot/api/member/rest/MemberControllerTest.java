package com.example.emotionbot.api.member.rest;

import com.example.emotionbot.api.challenge.service.ChallengeService;
import com.example.emotionbot.api.member.dto.request.SignUpRequest;
import com.example.emotionbot.api.member.entity.KeyboardYn;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.entity.PushYn;
import com.example.emotionbot.api.member.entity.TalkType;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.api.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ChallengeService challengeService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberService memberService;

    @Test
    void createAccount_성공_테스트() {
//        // given
//        SignUpRequest signUpRequest = new SignUpRequest("testLoginId", "testPassword", "nickname", KeyboardYn.Y, TalkType.T);
//        String encodedPassword = "encodedPassword";
//
//        when(memberRepository.existsByLoginId(signUpRequest.loginId())).thenReturn(false);
//        when(passwordEncoder.encode(signUpRequest.password())).thenReturn(encodedPassword);
//
//        Member savedMember = Member.builder()
//                .loginId(signUpRequest.loginId())
//                .password(passwordEncoder.encode(signUpRequest.password()))
//                .nickname(signUpRequest.nickname())
//                .clover(0)
//                .talkType(signUpRequest.talkType())
//                .keyboardYn(signUpRequest.keyboardYn())
//                .pushYn(PushYn.Y)
//                .is_deleted(false)
//                .build();
//
//        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
//
//        // when
//        Long createdMemberId = memberService.createAccount(signUpRequest);
//
//        // then
//        assertThat(createdMemberId).isEqualTo(savedMember.getId());
//        verify(memberRepository).existsByLoginId(signUpRequest.loginId());
//        verify(passwordEncoder).encode(signUpRequest.password());
//        verify(memberRepository).save(any(Member.class));
//        verify(challengeService).createChallenge(savedMember.getId());
    }
}
