package com.example.emotionbot.api.dailySummary.dto.req;

import lombok.Getter;

import java.time.LocalDate;


@Getter
public class DiaryRequestDto {
    String diary;
    LocalDate date;
}
