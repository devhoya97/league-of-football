package com.lof.global.exception;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        // TODO: 이런식으로 예외를 넘겨줘서 처리하려고 했는데, 그럼 AuthInterceptor에서 preHandle을 수행하다가 예외가 발생하면
        //  afterCompletion이 호출이 안돼서 로깅이 안 됨
        //  로깅필터 -> auth필터 순으로 호출하면, auth 필터에서 예외가 발생해도 로깅필터에서 이를 찍을 수 있을 것 같긴 한데...
        //  memberId를 로깅에 포함시키고 싶은거라면... 로깅필터에서 response에만 memberId를 찍어줄까?
        request.setAttribute("error", e);
//        log.error("code: {}", e.getCode().toString(), e);
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
//        log.error("code: {}, message: {}", ErrorCode.INVALID_USER_INPUT.toString(), ErrorCode.INVALID_USER_INPUT.getMessage(), e);
        return new ErrorResponse(ErrorCode.INVALID_USER_INPUT.toString(), message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(Exception e, HttpServletRequest request) {
        request.setAttribute("error", e);
//        log.error("code: {}", HttpStatus.INTERNAL_SERVER_ERROR.toString(), e); // requestID를 찍을 수가 없네 -> MDC?
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "서버에서 예기치 못한 에러가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }
}
