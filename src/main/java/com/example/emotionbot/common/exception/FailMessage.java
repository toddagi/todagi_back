package com.example.emotionbot.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FailMessage {
    //400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 40000, "잘못된 요청입니다."),
    BAD_REQUEST_REQUEST_BODY_VALID(HttpStatus.BAD_REQUEST, 40001, "잘못된 요청본문입니다."),
    BAD_REQUEST_MISSING_PARAM(HttpStatus.BAD_REQUEST, 40002, "필수 파라미터가 없습니다."),
    BAD_REQUEST_METHOD_ARGUMENT_TYPE(HttpStatus.BAD_REQUEST, 40003, "메서드 인자타입이 잘못되었습니다."),
    BAD_REQUEST_NOT_READABLE(HttpStatus.BAD_REQUEST, 40004, "Json 오류 혹은 요청본문 필드 오류 입니다. "),

    //401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 40100, "인증이 필요합니다."),
    UNAUTHORIZED_EXPIRED(HttpStatus.UNAUTHORIZED, 40101, "토큰 기간이 만료 되었습니다."),
    UNAUTHORIZED_EMPTY_HEADER(HttpStatus.UNAUTHORIZED, 40102, "인증 정보가 없습니다."),
    UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 40102, "토큰의 정보가 올바르지 않습니다."),

    //403
    FORBIDDEN(HttpStatus.FORBIDDEN, 40300, "권한이 없습니다."),

    //404
    NOT_FOUND(HttpStatus.NOT_FOUND, 40400, "리소스를 찾을 수 없습니다."),
    NOT_FOUND_API(HttpStatus.NOT_FOUND, 40401, "잘못된 API입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 40402, "유저를 찾을 수 없습니다."),


    //405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 40500, "잘못된 HTTP 메소드 요청입니다."),

    //409
    CONFLICT(HttpStatus.CONFLICT, 40900, "서버의 현재 상태와 요청이 충돌했습니다."),
    CONFLICT_INTEGRITY(HttpStatus.CONFLICT, 40901, "데이터 무결성 위반입니다."),
    CONFLICT_DUPLICATE_ID(HttpStatus.CONFLICT, 40902, "이미 존재하는 아이디입니다."),
    CONFLICT_WRONG_PW(HttpStatus.CONFLICT, 40903, "비밀번호가 일치하지 않습니다"),
    CONFLICT_NO_ID(HttpStatus.CONFLICT, 40904, "존재하지 않는 아이디입니다"),
    CONFLICT_NO_CHALLENGE(HttpStatus.CONFLICT, 40905, "존재하지 않는 챌린지입니다"),


    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "서버 내부 오류가 발생했습니다."),
    INTERNAL_TOKEN_INIT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "SECRET KEY가 초기화되지 않았습니다."),
    INTERNAL_AI_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "AI 서버 요청 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
