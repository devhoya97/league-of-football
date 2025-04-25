package com.lof.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** TODO: 도메인별로 분리하기 -> MSA 관점에서도 자신의 Enum만 가지고 있으면 되겠다.
 *
 * public enum AuthErrorCode implements ErrorCode {
 *     INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
 *     ...
 * }
 *
 * public enum MemberErrorCode implements ErrorCode {
 *     DUPLICATED_USERNAME("이미 존재하는 회원 이름입니다.", HttpStatus.BAD_REQUEST),
 *     ...
 * }
 *
 * public enum QueueErrorCode implements ErrorCode {
 *     NOT_EXIST_WAITING_QUEUE("대기열이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
 *     ...
 * }
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // DTO의 @Valid에서 예외 발생한 경우 기본 메시지
    INVALID_USER_INPUT("유효하지 사용자 입력입니다.", HttpStatus.BAD_REQUEST),
    // 요청 관련
    INVALID_REQUEST_BODY("요청 본문이 형식에 맞지 않아 읽을 수 없습니다.", HttpStatus.BAD_REQUEST),

    // 랭크 티어
    INVALID_RANK_TIER("존재하지 않는 랭크 티어입니다.", HttpStatus.BAD_REQUEST),

    // 지역
    INVALID_DISTRICT("존재하지 않는 지역 명입니다.", HttpStatus.BAD_REQUEST),

    // 회원
    DUPLICATED_USERNAME("이미 존재하는 회원 이름입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_MEMBER("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST),

    // 로그인, 토큰
    INVALID_LOGIN("회원 이름 또는 비밀번호를 다시 확인해주세요.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    MISSING_TOKEN("요청에 토큰을 포함해주세요.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_REQUIRED("요청에 accessToken을 포함해주세요.", HttpStatus.UNAUTHORIZED),

    // 대기열
    NOT_EXIST_WAITING_QUEUE("대기열이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_WAITING("이미 대기열에 참가 중입니다.", HttpStatus.BAD_REQUEST),
    NOT_IN_WAITING("대기열에 참가 중이 아닙니다.", HttpStatus.BAD_REQUEST),
    INVALID_WAITING_QUEUE("이미 매칭이 완료됐거나 취소된 대기열입니다.", HttpStatus.BAD_REQUEST),
    LOCK_WAITING("잠시 후 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),

    FULL_WAITING_QUEUE_INTERNAL_SERVER_ERROR("대기열이 가득차 회원이 추가로 참여할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR), //TODO: 대기열id도 찍어주면 좋을 것 같다.

    // 게임
    NOT_FOUND_GAME("게임을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_GAME_MEMBER("게임이 존재하지 않거나 참여 중인 회원이 아닙니다.", HttpStatus.BAD_REQUEST),

    // 임시
    INTERNAL_SERVER_ERROR("서버에서 예기치 못한 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}
