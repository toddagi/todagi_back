package com.example.emotionbot.api.chat.rest;

import com.example.emotionbot.api.chat.dto.request.ChatRequest;
import com.example.emotionbot.api.chat.dto.request.ChatResponse;
import com.example.emotionbot.api.chat.entity.Chat;
import com.example.emotionbot.api.chat.entity.ChatType;
import com.example.emotionbot.api.chat.entity.Sender;
import com.example.emotionbot.api.chat.service.ChatService;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;
    private final ChatService chatService;

    @MessageMapping("/enter") // /app/enter
    public void enter(ChatRequest chatRequest) {
        log.info("✅ WebSocket 입장 요청 수신됨! chatRequest={}", chatRequest);
        Member member = memberRepository.findById(chatRequest.memberId())
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        // 환영 메시지 생성
        Chat botMessage = Chat.builder()
                .member(member)
                .message("환영합니다")
                .sender(Sender.BOT)
                .type(ChatType.ENTER)
                .sendTime(LocalDateTime.now())
                .build();

        // DB에 저장
        chatService.saveChat(botMessage);

        // DTO 변환 후 클라이언트로 전송
        ChatResponse response = ChatResponse.builder()
                .memberId(member.getId())
                .message("환영합니다")
                .sender(Sender.BOT)
                .build();

        messagingTemplate.convertAndSend("/topic/chat", response);
    }
}
