package com.example.emotionbot.api.dailySummary.rest;

import com.example.emotionbot.api.dailySummary.dto.req.DiaryRequest;
import com.example.emotionbot.api.dailySummary.service.DiaryService;
import com.example.emotionbot.common.utils.JwtTokenUtilV2;
import com.example.emotionbot.config.WebMvcConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
@Import(WebMvcConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class DiaryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private DiaryService diaryService;

    @MockBean
    private JwtTokenUtilV2 jwtTokenUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("커스텀한 ArgumentResolver이 작동하는지 확인한다")
    void createDiary() throws Exception {
        given(jwtTokenUtil.getPayload("valid.token")).willReturn("123");

        DiaryRequest request = new DiaryRequest(
                3,
                "diary test",
                LocalDate.now());

        mockMvc.perform(post("/diary/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid.token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(diaryService).saveDiary(idCaptor.capture(), any(DiaryRequest.class));
        assertThat(idCaptor.getValue()).isEqualTo(123L);

    }


}