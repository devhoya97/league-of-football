package com.lof.waitingqueue.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.lof.global.BaseEntity;
import com.lof.member.domain.Member;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WaitingQueueMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_queue_id")
    private WaitingQueue waitingQueue;

    @Enumerated(EnumType.STRING)
    private WaitingQueueMemberStatus waitingQueueMemberStatus = WaitingQueueMemberStatus.WAITING;

    public WaitingQueueMember(Member member, WaitingQueue waitingQueue) {
        this.member = member;
        this.waitingQueue = waitingQueue;
    }

    public void leaveWaitingQueue() {
        waitingQueueMemberStatus = WaitingQueueMemberStatus.LEAVE;
    }
}
