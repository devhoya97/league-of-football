package com.lof.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.lof.global.BaseEntity;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.waitingqueue.domain.WaitingQueue;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    private static final int INITIAL_RANK_SCORE = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private int rankScore = INITIAL_RANK_SCORE; // 이렇게 초기화해놔도 되나?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "queue_id")
    private WaitingQueue queue;

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void joinInWaitingQueue(WaitingQueue queue) {
        if (isAlreadyInWaitingQueue()) {
            throw new BizException(ErrorCode.ALREADY_WAITING);
        }
        this.queue = queue;
    }

    public void leaveWaitingQueue() {
        if (this.queue == null) {
            throw new BizException(ErrorCode.NOT_IN_WAITING);
        }
        this.queue = null;
    }

    public void increaseRankScore(int amount) {
        rankScore += amount;
    }

    public boolean isAlreadyInWaitingQueue() {
        return this.queue != null;
    }
}
