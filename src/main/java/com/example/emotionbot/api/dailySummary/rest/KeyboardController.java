package com.example.emotionbot.api.dailySummary.rest;

import com.example.emotionbot.api.dailySummary.dto.req.KeyboardRequest;
import com.example.emotionbot.api.dailySummary.entity.Keyboard;
import com.example.emotionbot.api.dailySummary.repository.KeyboardRepository;
import com.example.emotionbot.common.resolver.MemberId;
import com.example.emotionbot.common.response.APISuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keyboard")
@Slf4j
public class KeyboardController {

    private final KeyboardRepository keyboardRepository;
    @Operation(summary = "키보드 데이터 요청", description = "한시간에 한번씩 데이터 요청")
    @PostMapping
    public ResponseEntity<APISuccessResponse<Long>> getData(@MemberId Long memberId,
                                                            @Parameter(description = "br 태그 붙여서 구별", example = "<br>오늘 지각함...<br>날씨가 좋네<br>")
                                                            @RequestBody KeyboardRequest keyboardRequest){
        Keyboard keyboard= Keyboard.builder().
                memberId(memberId).
                text(keyboardRequest.text())
                .build();

        keyboardRepository.save(keyboard);

        return ResponseEntity.ok(APISuccessResponse.ofSuccess(keyboard.getId()));
    }
}
