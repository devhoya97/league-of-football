package com.lof.waitingqueue.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lof.waitingqueue.controller.dto.JoinRequest;
import com.lof.waitingqueue.controller.dto.JoinResponse;
import com.lof.waitingqueue.controller.dto.MatchingStatusResponse;
import com.lof.waitingqueue.service.JoinResult;
import com.lof.waitingqueue.service.WaitingQueueService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/queue")
@RestController
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    //TODO: requestBody 안적었을 때, HttpMessageNotReadableException이 500으로 나가는거 해결
    //TODO: 10명 들어왔을 때 게임 생성되는지 확인
    @PostMapping("/join")
    public JoinResponse join(@RequestAttribute long memberId, @Valid @RequestBody JoinRequest joinRequest) {
        JoinResult joinResult = waitingQueueService.join(memberId, joinRequest.gameDate());
        if (joinResult.game() == null) {
            return new JoinResponse(joinResult.waitingQueue().getId(), null);
        }
        return new JoinResponse(joinResult.waitingQueue().getId(), joinResult.game().getId()); //TODO 테스트부터 이어서
    }

    @PostMapping("/leave/{queueId}")
    public void leave(@RequestAttribute long memberId, @PathVariable long queueId) {
        waitingQueueService.leave(memberId, queueId);
    }

    // TODO: POST로 했을 때, 지원되지 않는 메서드면 관련된 예외 내보내도록
    // TODO: 테스트 코드 작성
    @GetMapping("/check/{queueId}")
    public MatchingStatusResponse checkMatchingStatus(@PathVariable long queueId) {
        JoinResult joinResult = waitingQueueService.checkMatchingStatus(queueId); // 일단 OSIV를 켠 상태인데, 성능때문에 이를 꺼야한다면, Service에서 반환하는 DTO가 API 스펙을 고려해야 할 것 같다.

        int joinedMemberCount = joinResult.waitingQueue().countJoinedMember();
        if (joinResult.game() == null) {
            return new MatchingStatusResponse(joinedMemberCount, null);
        }
        return new MatchingStatusResponse(joinedMemberCount, joinResult.game().getId());
    }
}
