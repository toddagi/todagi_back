package com.example.emotionbot.api.chat.service;

import com.example.emotionbot.api.chat.dto.request.ChatEnterRequestToAI;
import com.example.emotionbot.api.chat.dto.request.ChatEnterResponseToAI;
import com.example.emotionbot.api.chat.dto.request.ChatSendRequestToAI;
import com.example.emotionbot.api.chat.dto.request.ChatSendResponseToAI;
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

    @Value("${spring.ai.server.chat.url}")
    private String aiChatUrl;
    @Value("${spring.ai.server.summary.url}")
    private String aiSummaryUrl;

    public List<String> askChatSummary(ChatEnterRequestToAI chatEnterRequestToAI) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ChatEnterRequestToAI> request = new HttpEntity<>(chatEnterRequestToAI, headers);

            ResponseEntity<ChatEnterResponseToAI> response = restTemplate.postForEntity(
                    aiSummaryUrl,
                    request,
                    ChatEnterResponseToAI.class
            );

            return response.getBody().summary();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmotionBotException(FailMessage.INTERNAL_AI_SERVER_ERROR);
        }
    }

    public List<String> askToAi(ChatSendRequestToAI chatSendRequestToAI) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ChatSendRequestToAI> request = new HttpEntity<>(chatSendRequestToAI, headers);

            ResponseEntity<ChatSendResponseToAI> response = restTemplate.postForEntity(
                    aiChatUrl,
                    request,
                    ChatSendResponseToAI.class
            );

            return response.getBody().message();
        } catch (Exception e) {
            throw new EmotionBotException(FailMessage.INTERNAL_AI_SERVER_ERROR);
        }
    }


}
