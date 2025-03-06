package com.lof.waitingqueue.service;

import com.lof.game.domain.Game;
import com.lof.waitingqueue.domain.WaitingQueue;

// 질문: 이런 식으로 서비스가 리턴하는 DTO에 도메인 객체를 넣는거 괜찮을까? 다 까서 보내는 것도 이상한 것 같아서..
public record JoinResult(WaitingQueue waitingQueue, Game game) {
}
