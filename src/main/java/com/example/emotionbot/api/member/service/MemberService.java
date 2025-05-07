package com.example.emotionbot.api.member.service;

import com.example.emotionbot.api.challenge.entity.ChallengeOption;
import com.example.emotionbot.api.challenge.service.ChallengeService;
import com.example.emotionbot.api.member.dto.request.ConsumeCloverRequest;
import com.example.emotionbot.api.member.dto.request.LoginRequest;
import com.example.emotionbot.api.member.dto.request.SignUpRequest;
import com.example.emotionbot.api.member.dto.response.LoginResponse;
import com.example.emotionbot.api.member.dto.response.LogoutResponse;
import com.example.emotionbot.api.member.dto.response.MemberInformationResponse;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import com.example.emotionbot.common.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
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
    private final ChallengeService challengeService;

    @Transactional
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

        Member savedMember = memberRepository.save(member);
        challengeService.createChallenge(savedMember.getId());
        return savedMember.getId();
    }

    @Transactional
    public LoginResponse login(@Valid LoginRequest loginRequest) {
        Member member = memberRepository.findByLoginId(loginRequest.loginId()).orElseThrow(() ->
                new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new EmotionBotException(FailMessage.CONFLICT_WRONG_PW);
        }

        String accessToken = jwtTokenUtil.createToken(loginRequest.loginId());
        String refreshToken = jwtTokenUtil.createRefreshToken(loginRequest.loginId());

        redisTemplate.opsForValue().set(
                member.getLoginId(),
                refreshToken,
                jwtTokenUtil.getRefreshTokenExpirationMillis(),
                TimeUnit.MILLISECONDS
        );

        challengeService.completeChallenge(member.getId(), ChallengeOption.ATTENDANCE);
        return LoginResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public LoginResponse reissueToken(String refreshToken) {
        refreshToken = jwtTokenUtil.extractTokenValue(refreshToken);
        Claims claims = jwtTokenUtil.verify(refreshToken);
        String loginId = claims.getSubject();

        String storedRefreshToken = redisTemplate.opsForValue().get(loginId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new EmotionBotException(FailMessage.BAD_REQUEST);
        }

        String newAccessToken = jwtTokenUtil.createToken(loginId);

        return new LoginResponse(newAccessToken, refreshToken);
    }

    @Transactional
    public LogoutResponse logOut(String refreshToken) {
        refreshToken = jwtTokenUtil.extractTokenValue(refreshToken);
        Claims claims = jwtTokenUtil.verify(refreshToken);
        String loginId = claims.getSubject();
        redisTemplate.delete(loginId);

        return new LogoutResponse(loginId + "님 로그아웃되었습니다");

    }

    @Transactional
    public MemberInformationResponse getMemberInformation(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));
        return new MemberInformationResponse(memberId, member.getNickname(), member.getClover(), member.getKeyboardYn(), member.getTalkType());
    }

    @Transactional
    public void consumeClover(Long memberId, ConsumeCloverRequest consumeCloverRequest) {
        int deleteClover = consumeCloverRequest.deleteClover();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));
        member.consumeClover(deleteClover);
    }

    @Transactional
    public void changeNickname(Long memberId, String nickName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));
        member.updateNickname(nickName);
    }

    @Transactional
    public void changeTalkType(Long memberId, int talkTypeValue) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));
        member.updateTalkType(talkTypeValue);
    }

    @Transactional
    public void changeKeyBoardYn(Long memberId, String keyBoardYn) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));
        member.updateKeyBoardYn(keyBoardYn);
    }
}
