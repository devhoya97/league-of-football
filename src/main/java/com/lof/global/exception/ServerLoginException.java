package com.lof.global.exception;

import lombok.Getter;

@Getter
public class ServerLoginException extends RuntimeException {

    private final ErrorCode code;

    public ServerLoginException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ServerLoginException(ErrorCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }

    public ServerLoginException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
