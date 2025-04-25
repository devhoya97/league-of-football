package com.lof.waitingqueue.controller;

import java.time.LocalDate;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lof.common.District;
import com.lof.member.domain.RankTier;
import com.lof.waitingqueue.controller.dto.CheckResponse;
import com.lof.waitingqueue.controller.dto.JoinRequest;
import com.lof.waitingqueue.controller.dto.JoinResponse;
import com.lof.waitingqueue.controller.dto.LeaveRequest;
import com.lof.waitingqueue.service.WaitingQueueService;
import com.lof.waitingqueue.service.WaitingQueueStatus;

import lombok.RequiredArgsConstructor;

@RequestMapping("/queues")
@RestController
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    @PostMapping("/join")
    public JoinResponse join(@Valid @RequestBody JoinRequest joinRequest,
                             @RequestAttribute long memberId) {
        WaitingQueueStatus waitingQueueStatus = waitingQueueService.join(memberId, joinRequest.date(), joinRequest.district());

        return new JoinResponse(waitingQueueStatus);
    }

    @PostMapping("/leave")
    public void leave(@Valid @RequestBody LeaveRequest leaveRequest,
                      @RequestAttribute long memberId) {
        waitingQueueService.leave(leaveRequest.date(), leaveRequest.rankTier(), leaveRequest.district(), memberId);
    }

    @GetMapping("/check")
    public CheckResponse checkMatchingStatus(@RequestParam LocalDate date,
                                             @RequestParam RankTier rankTier,
                                             @RequestParam District district) {
        int waitingQueueSize = waitingQueueService.checkWaitingQueueSize(date, rankTier, district);
        return new CheckResponse(waitingQueueSize);
    }
}
