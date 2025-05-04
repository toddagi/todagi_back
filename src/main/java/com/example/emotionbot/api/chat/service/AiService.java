package com.example.emotionbot.api.chat.service;

import com.example.emotionbot.api.chat.dto.request.ChatSendRequest;
import com.example.emotionbot.api.chat.dto.request.ChatSendRequestToAI;
import com.example.emotionbot.api.chat.dto.request.ChatSendResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;

    @Value("${spring.ai.server.url}")
    private String aiServerUrl;

    public List<String> askToAi(ChatSendRequestToAI chatSendRequestToAI) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ChatSendRequestToAI> request = new HttpEntity<>(chatSendRequestToAI, headers);

            ResponseEntity<ChatSendResponse> response = restTemplate.postForEntity(
                    aiServerUrl,
                    request,
                   ChatSendResponse.class
            );

            return response.getBody().message();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmotionBotException(FailMessage.INTERNAL_AI_SERVER_ERROR);
        }
    }

}
