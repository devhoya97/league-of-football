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
    ;

    private final String message;

    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}
