package com.example.emotionbot.api.chat.service;

import com.example.emotionbot.api.chat.dto.request.ChatSendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;

    @Value("${spring.ai.server.url}")
    private String aiServerUrl;

    public String askToAi(ChatSendRequest chatSendRequest) {
        try {
            log.info("💬 사용자 메시지 수신21: {}", chatSendRequest);
            HttpHeaders headers = new HttpHeaders();
            log.info("💬 사용자 메시지 수신22: {}", chatSendRequest);
            headers.setContentType(MediaType.APPLICATION_JSON);
            log.info("💬 사용자 메시지 수신23: {}", chatSendRequest);
            HttpEntity<ChatSendRequest> request = new HttpEntity<>(chatSendRequest, headers);
            log.info("💬 사용자 메시지 수신24: {}", chatSendRequest);
            ResponseEntity<String> response = restTemplate.postForEntity(aiServerUrl, request, String.class);
            log.info("💬 사용자 메시지 수신25: {}", chatSendRequest);
            return response.getBody();
        } catch (Exception e) {
            log.error("❌ AI 서버 요청 중 예외 발생", e);
            return "AI 서버 요청 중 오류가 발생했습니다.";
        }
    }

}
