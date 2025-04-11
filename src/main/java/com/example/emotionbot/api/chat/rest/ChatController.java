package com.example.emotionbot.api.chat.rest;

import com.example.emotionbot.api.chat.dto.request.ChatEnterRequest;
import com.example.emotionbot.api.chat.dto.request.ChatEnterResponse;
import com.example.emotionbot.api.chat.dto.request.ChatSendRequest;
import com.example.emotionbot.api.chat.entity.Chat;
import com.example.emotionbot.api.chat.entity.ChatType;
import com.example.emotionbot.api.chat.entity.Sender;
import com.example.emotionbot.api.chat.service.AiService;
import com.example.emotionbot.api.chat.service.ChatService;
import com.example.emotionbot.api.member.entity.Member;
import com.example.emotionbot.api.member.repository.MemberRepository;
import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import com.example.emotionbot.common.response.APISuccessResponse;
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
    private final AiService aiService;

    @MessageMapping("/enter") // /app/enter
    public void enter(ChatEnterRequest chatEnterRequest) {
        Member member = memberRepository.findById(chatEnterRequest.memberId())
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
        ChatEnterResponse response = ChatEnterResponse.builder()
                .memberId(member.getId())
                .message("환영합니다")
                .sender(Sender.BOT)
                .build();

        messagingTemplate.convertAndSend("/topic/chat", APISuccessResponse.ofSuccess(response));
    }

    @MessageMapping("/send") // /app/send
    public void sendMessage(ChatSendRequest chatSendRequest) {

        Member member = memberRepository.findById(chatSendRequest.memberId())
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));

        // 1. 사용자 메시지 DB 저장
        Chat userMessage = Chat.builder()
                .member(member)
                .message(chatSendRequest.message())
                .sender(Sender.USER)
                .type(ChatType.SEND)
                .sendTime(LocalDateTime.now())
                .build();

        chatService.saveChat(userMessage);

         //2. 사용자 메시지 프론트에 전송
        ChatEnterResponse userResponse = ChatEnterResponse.builder()
                .memberId(member.getId())
                .message(userMessage.getMessage())
                .sender(Sender.USER)
                .build();
        messagingTemplate.convertAndSend("/topic/chat", userResponse);
        log.info("💬 사용자 메시지 수신2: {}", chatSendRequest);
        // 3. AI 서버에 REST API 호출
        String aiResponseText = aiService.askToAi(chatSendRequest);
        log.info("💬 사용자 메시지 수신3: {}", chatSendRequest);


        // 4. AI 응답 DB 저장
        Chat aiMessage = Chat.builder()
                .member(member)
                .message(aiResponseText)
                .sender(Sender.BOT)
                .type(ChatType.SEND)
                .sendTime(LocalDateTime.now())
                .build();

        chatService.saveChat(aiMessage);
        log.info("💬 사용자 메시지 수신4: {}", chatSendRequest);

        // 5. AI 응답 프론트에 전송
        ChatEnterResponse botResponse = ChatEnterResponse.builder()
                .memberId(member.getId())
                .message(aiMessage.getMessage())
                .sender(Sender.BOT)
                .build();
        log.info("💬 사용자 메시지 수신5: {}", chatSendRequest);

        messagingTemplate.convertAndSend("/topic/chat", botResponse);
        log.info("💬 사용자 메시지 수신6: {}", chatSendRequest);
    }

}
