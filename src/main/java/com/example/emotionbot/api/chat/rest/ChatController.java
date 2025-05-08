package com.example.emotionbot.api.chat.rest;

import com.example.emotionbot.api.challenge.entity.ChallengeOption;
import com.example.emotionbot.api.challenge.service.ChallengeService;
import com.example.emotionbot.api.chat.dto.request.ChatEnterRequest;
import com.example.emotionbot.api.chat.dto.request.ChatEnterResponse;
import com.example.emotionbot.api.chat.dto.request.ChatSendRequest;
import com.example.emotionbot.api.chat.dto.request.ChatSendRequestToAI;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;
    private final ChallengeService challengeService;
    private final ChatService chatService;
    private final AiService aiService;

    @MessageMapping("/enter")
    public void enter(ChatEnterRequest chatEnterRequest) {
        Member member = findMember(chatEnterRequest.memberId());

        Chat botMessage = chatService.createChat(member, "환영합니다", Sender.BOT, ChatType.ENTER);
        chatService.saveChat(botMessage);

        ChatEnterResponse response = createResponse(member.getId(), botMessage.getMessage(), Sender.BOT);
        messagingTemplate.convertAndSend("/topic/chat", APISuccessResponse.ofSuccess(response));
    }

    @MessageMapping("/send")
    public void sendMessage(ChatSendRequest chatSendRequest) {
        Member member = findMember(chatSendRequest.memberId());
        challengeService.completeChallenge(chatSendRequest.memberId(), ChallengeOption.CHAT);

        // 사용자 메시지 저장 및 전송
        Chat userMessage = chatService.createChat(member, chatSendRequest.message(), Sender.USER, ChatType.SEND);
        chatService.saveChat(userMessage);
        sendToClient(userMessage);

        // AI 서버 호출 및 응답 처리
        ChatSendRequestToAI chatSendRequestToAI = new ChatSendRequestToAI(chatSendRequest.memberId(), chatSendRequest.message(), member.getTalkType().getTalkTypeString());
        List<String> aiResponseTexts = aiService.askToAi(chatSendRequestToAI);

        for (String responseText : aiResponseTexts) {
            Chat aiMessage = chatService.createChat(member, responseText, Sender.BOT, ChatType.SEND);
            chatService.saveChat(aiMessage);
            sendToClient(aiMessage);
        }
    }

    private Member findMember(Long memberId) {
        return memberRepository.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new EmotionBotException(FailMessage.CONFLICT_NO_ID));
    }

    private void sendToClient(Chat chat) {
        ChatEnterResponse response = createResponse(chat.getMember().getId(), chat.getMessage(), chat.getSender());
        messagingTemplate.convertAndSend("/topic/chat", response);
    }

    private ChatEnterResponse createResponse(Long memberId, String message, Sender sender) {
        return ChatEnterResponse.builder()
                .memberId(memberId)
                .message(message)
                .sender(sender)
                .build();
    }
}
