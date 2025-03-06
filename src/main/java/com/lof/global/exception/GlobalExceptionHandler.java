package com.lof.global.exception;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequestException(BizException e, HttpServletRequest request) {
        request.setAttribute("error", e);
        ErrorResponse errorResponse = new ErrorResponse(e.getCode().toString(), e.getMessage());

        return ResponseEntity.status(e.getCode().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = Optional.ofNullable(e.getBindingResult().getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(ErrorCode.INVALID_USER_INPUT.getMessage());

        request.setAttribute("error", new BizException(ErrorCode.INVALID_USER_INPUT, message, e));
        return new ErrorResponse(ErrorCode.INVALID_USER_INPUT.toString(), message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(HttpMessageNotReadableException e, HttpServletRequest request) {
        request.setAttribute("error", new BizException(ErrorCode.INVALID_REQUEST_BODY, e));
        return new ErrorResponse(ErrorCode.INVALID_REQUEST_BODY.toString(), ErrorCode.INVALID_REQUEST_BODY.getMessage());
    }

    // TODO: API의 HTTP Method를 잘못 매칭해서 호출하면 GlobalExceptionHandler는 호출되는데 LogInterceptor는 호출되지 않음. 왜일까?
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(Exception e, HttpServletRequest request) {
        request.setAttribute("error", e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "서버에서 예기치 못한 에러가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }
}
