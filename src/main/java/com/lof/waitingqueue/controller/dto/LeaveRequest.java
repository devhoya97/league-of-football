package com.lof.waitingqueue.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import com.lof.common.District;
import com.lof.member.domain.RankTier;

public record LeaveRequest(
        @FutureOrPresent(message = "게임 날짜는 현재 날짜 이상이어야 합니다.")
        LocalDate date,

        RankTier rankTier,

        @NotNull(message = "지역을 입력해주세요.")
        District district
) {
}
