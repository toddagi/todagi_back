package com.example.emotionbot.api.dailySummary.dto.res;

import java.time.LocalDate;

public record DiaryResponse(int feeling, String diary, LocalDate date,String summary,
                            float angry, float annoy, float sleepy,float good, float happy) {
}
