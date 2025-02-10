package com.lof.global.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    protected final ErrorCode code;

    public AuthException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public AuthException(ErrorCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }
}
