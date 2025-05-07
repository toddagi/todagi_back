package com.example.emotionbot.api.member.entity;

import com.example.emotionbot.common.exception.EmotionBotException;
import com.example.emotionbot.common.exception.FailMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @Column(name = "login_id", columnDefinition = "varchar(100)", nullable = false)
    private String loginId;

    @Column(name = "password", columnDefinition = "varchar(100)", nullable = false)
    private String password;

    @Column(name = "nickname", columnDefinition = "varchar(100)", nullable = false)
    private String nickname;

    @Column(name = "clover", columnDefinition = "bigint(100)", nullable = false)
    private int clover;

    @Column(name = "keyboard_yn", columnDefinition = "varchar(5)", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private KeyboardYn keyboardYn;

    @Column(name = "talk_mode", columnDefinition = "varchar(5)", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TalkType talkType;

    @Column(name = "push_yn", columnDefinition = "varchar(5)", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PushYn pushYn = PushYn.Y;

    @Column(name = "is_deleted", columnDefinition = "boolean", nullable = false)
    private boolean isDeleted = false;

    @Builder
    public Member(String loginId, String password, String nickname, int clover, KeyboardYn keyboardYn, TalkType talkType, PushYn pushYn, boolean is_deleted) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.clover = clover;
        this.keyboardYn = keyboardYn;
        this.talkType = talkType;
        this.pushYn = pushYn;
        this.isDeleted = is_deleted;
    }

    public void updateClover(int addClover) {
        this.clover += addClover;
    }

    public void consumeClover(int deleteClover) {
        if (this.clover - deleteClover < 0) {
            throw new EmotionBotException(FailMessage.CONFLICT_NO_CLOVER);
        }
        this.clover -= deleteClover;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateTalkType(int talkTypeValue){
        for (TalkType type : TalkType.values()) {
            if (type.getTalkTypeValue() == talkTypeValue) {
                this.talkType = type;
                return;
            }
        }
        throw new EmotionBotException(FailMessage.CONFLICT_NO_TALK_TYPE);
    }

    public void updateKeyBoardYn(String keyBoardYn) {
        try {
            this.keyboardYn = KeyboardYn.valueOf(keyBoardYn);
        } catch (IllegalArgumentException e) {
            throw new EmotionBotException(FailMessage.CONFLICT_NO_YN);
        }
    }

    public void updateIsDeleted(){
        this.isDeleted = true;
    }

    public void updatePushYn(String pushYn){
        try {
            this.pushYn = PushYn.valueOf(pushYn);
        } catch (IllegalArgumentException e) {
            throw new EmotionBotException(FailMessage.CONFLICT_NO_YN);
        }
    }

}

