package com.lof.waitingqueue.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import com.lof.common.District;

public record LeaveResponse(
        boolean success
) {
}
