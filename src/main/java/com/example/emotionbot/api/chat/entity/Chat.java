package com.example.emotionbot.api.chat.entity;

import com.example.emotionbot.api.chatRoom.entity.ChatRoom;
import com.example.emotionbot.api.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @JoinColumn(name = "chat_room_id", columnDefinition = "bigint(20)", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @JoinColumn(name = "member_id", columnDefinition = "bigint(20)")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "message", columnDefinition = "varchar(100)", nullable = false)
    private String message;

    @Column(name = "sender", columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sender sender;

    @Column(name = "send_time", columnDefinition = "timestamp", nullable = false)
    private LocalDateTime sendTime;

    @Builder
    public Chat(Long id, ChatRoom chatRoom, Member member, String message, Sender sender,LocalDateTime sendTime) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.member = member;
        this.message = message;
        this.sender=sender;
        this.sendTime = sendTime;
    }
}
