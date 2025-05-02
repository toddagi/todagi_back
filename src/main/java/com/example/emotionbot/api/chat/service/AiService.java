package com.example.emotionbot.api.chat.service;

import com.example.emotionbot.api.chat.dto.request.ChatSendRequest;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;

    @Value("${spring.ai.server.url}")
    private String aiServerUrl;

    public String askToAi(ChatSendRequest chatSendRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ChatSendRequest> request = new HttpEntity<>(chatSendRequest, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(aiServerUrl, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmotionBotException(FailMessage.INTERNAL_AI_SERVER_ERROR);
        }
    }
}
