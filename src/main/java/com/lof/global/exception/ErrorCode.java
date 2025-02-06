package com.lof.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // @NotBlank 또는 @NotNull 검증
    NULL_OR_BLANK_PARAMETER("입력 값이 비어있거나 공백으로만 이루어져 있습니다."),

    // 회원가입
    DUPLICATED_LOGINID("이미 존재하는 회원 아이디입니다."),
    INVALID_LOGINID("아이디는 5~20자의 영문과 숫자로 구성되어야 하며, 숫자로만 이루어질 수 없습니다."),
    INVALID_PASSWORD("비밀번호는 8~20자이며, 대문자, 소문자, 숫자, 특수문자(!@#$%^&*)를 최소 하나씩 포함해야 합니다."),
    ;

    private final String message;

    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}
