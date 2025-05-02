package com.example.emotionbot.api.chat.service;

import com.example.emotionbot.api.chat.entity.Chat;
import com.example.emotionbot.api.chat.entity.ChatType;
import com.example.emotionbot.api.chat.entity.Sender;
import com.example.emotionbot.api.chat.repository.ChatRepository;
import com.example.emotionbot.api.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat saveChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Chat createChat(Member member, String message, Sender sender, ChatType type) {
        return Chat.builder()
                .member(member)
                .message(message)
                .sender(sender)
                .type(type)
                .sendTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
    }
}
