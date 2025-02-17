package com.lof.global.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    protected final ErrorCode code;

    public BizException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public BizException(ErrorCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }
}
