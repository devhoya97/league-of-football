package com.lof.waitingqueue.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lof.waitingqueue.controller.dto.JoinResponse;
import com.lof.waitingqueue.service.WaitingQueueService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/queue")
@RestController
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    @PostMapping("/join")
    public JoinResponse join(@RequestAttribute long memberId) {
        long waitingQueueId = waitingQueueService.join(memberId);
        return new JoinResponse(waitingQueueId);
    }

    @PostMapping("/leave")
    public void leave(@RequestAttribute long memberId) {
        waitingQueueService.leave(memberId);
    }
}
