package com.example.emotionbot.api.dailySummary.dto.res;

import java.time.LocalDate;

public record DiaryResponse(int feeling, String diary, LocalDate date) {
}
