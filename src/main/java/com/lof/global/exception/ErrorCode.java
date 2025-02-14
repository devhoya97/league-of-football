package com.lof.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /*
    int 값을 넘겨줄까 고민해봤는데, 토스페이먼츠 API를 보니까 에러코드를 String으로 약속하고
    클라이언트 측에서 String을 약속된대로 파싱하는게 가독성이 좋지 않을까 생각했음
    https://docs.tosspayments.com/reference/error-codes#%EA%B2%B0%EC%A0%9C-%EC%8A%B9%EC%9D%B8
     */

    // DTO의 @Valid에서 예외 발생한 경우 기본 메시지
    INVALID_USER_INPUT("유효하지 사용자 입력입니다."),

    // 회원가입
    DUPLICATED_LOGINID("이미 존재하는 회원 아이디입니다."),

    // 로그인
    INVALID_LOGIN("아이디 또는 비밀번호를 다시 확인해주세요."),
    INVALID_TOKEN("토큰이 유효하지 않습니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    MISSING_TOKEN("요청에 토큰을 포함해주세요."),
    ;

    private final String message;

    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}
