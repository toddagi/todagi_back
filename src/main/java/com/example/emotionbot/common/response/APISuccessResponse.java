package com.example.emotionbot.common.response;

import org.springframework.http.HttpStatus;

public record APISuccessResponse<T>(String message, Integer statusCode, T data) {
    private static final String SUCCESS = "Success";

    public static <T> APISuccessResponse<T> of(HttpStatus statusCode, String message, T data) {
        return new APISuccessResponse<>(message, statusCode.value(), data);
    }

    public static <T> APISuccessResponse<T> ofSuccess(T data) {
        return new APISuccessResponse<>(SUCCESS, HttpStatus.OK.value(), data);
    }

    public static <T> APISuccessResponse<T> ofCreateSuccess(T data) {
        return new APISuccessResponse<>(SUCCESS, HttpStatus.CREATED.value(), data);
    }
}