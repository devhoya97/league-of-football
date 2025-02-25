package com.lof.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // DTO의 @Valid에서 예외 발생한 경우 기본 메시지
    INVALID_USER_INPUT("유효하지 사용자 입력입니다.", HttpStatus.BAD_REQUEST),

    // 회원가입
    DUPLICATED_USERNAME("이미 존재하는 회원 이름입니다.", HttpStatus.BAD_REQUEST),

    // 로그인
    INVALID_LOGIN("회원 이름 또는 비밀번호를 다시 확인해주세요.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    MISSING_TOKEN("요청에 토큰을 포함해주세요.", HttpStatus.UNAUTHORIZED),

    // 대기열
    ALREADY_WAITING("이미 대기열에 참가 중입니다.", HttpStatus.BAD_REQUEST),
    NOT_IN_WAITING("대기열에 참가 중이 아닙니다.", HttpStatus.BAD_REQUEST),
    ALREADY_COMPLETED_WAITING_QUEUE("이미 매칭이 완료된 대기열입니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String message;
    private final HttpStatus httpStatus;

    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}
