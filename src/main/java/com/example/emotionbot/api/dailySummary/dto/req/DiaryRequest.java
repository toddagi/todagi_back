package com.example.emotionbot.api.dailySummary.dto.req;


import java.time.LocalDate;


public record DiaryRequest(
        String diary,
        LocalDate date
) {

}
