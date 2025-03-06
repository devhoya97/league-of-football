package com.lof.waitingqueue.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;

public record JoinRequest(
        @FutureOrPresent(message = "게임 날짜는 현재 날짜 이상이어야 합니다.")
        LocalDate gameDate
) {
}
