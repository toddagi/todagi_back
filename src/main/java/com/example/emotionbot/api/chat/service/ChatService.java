package com.example.emotionbot.api.chat.service;

import com.example.emotionbot.api.chat.entity.Chat;
import com.example.emotionbot.api.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    public Chat saveChat(Chat chat) {
        return chatRepository.save(chat);
    }
}
