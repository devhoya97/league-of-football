package com.lof.waitingqueue.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.lof.global.BaseEntity;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO: 평균점수는 Member 테이블과 join해서 구하는데, 이 성능이 느리다면 여기서 필드로 저장하는 방법 고려
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WaitingQueue extends BaseEntity {

    private static final int MAX_JOINED_WAITING_QUEUE_MEMBER_COUNT = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "waitingQueue", cascade = CascadeType.PERSIST)
    private List<WaitingQueueMember> waitingQueueMembers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private WaitingQueueStatus status = WaitingQueueStatus.MATCHING;

    private LocalDate gameDate;

    public WaitingQueue(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public void joinMember(Member member) {
        validateInMatching();
        List<WaitingQueueMember> joinedWaitingQueueMember = filterJoinedMembers();
        validateSize(joinedWaitingQueueMember.size());
        if (joinedWaitingQueueMember.size() + 1 == MAX_JOINED_WAITING_QUEUE_MEMBER_COUNT) {
            status = WaitingQueueStatus.COMPLETED;
        }
        waitingQueueMembers.add(new WaitingQueueMember(member, this));
    }

    private void validateInMatching() {
        if (status != WaitingQueueStatus.MATCHING) {
            throw new BizException(ErrorCode.INVALID_WAITING_QUEUE);
        }
    }

    private void validateSize(int joinedWaitingQueueMemberCount) {
        if (joinedWaitingQueueMemberCount >= MAX_JOINED_WAITING_QUEUE_MEMBER_COUNT) {
            throw new BizException(ErrorCode.FULL_WAITING_QUEUE_INTERNAL_SERVER_ERROR);
        }
    }

    public void decideTeam() {
        List<WaitingQueueMember> gameMembers = filterJoinedMembers();
        Collections.sort(gameMembers, (wqm1, wqm2) -> wqm1.getMember().getRankScore() - wqm2.getMember().getRankScore());
        for (int i = 0; i < MAX_JOINED_WAITING_QUEUE_MEMBER_COUNT; i++) {
            WaitingQueueMember waitingQueueMember = gameMembers.get(i);
            if (i % 2 == 0) {
                waitingQueueMember.belongToBlueTeam();
            } else {
                waitingQueueMember.belongToRedTeam();
            }
        }
    }

    public void completeMatching() {
        status = WaitingQueueStatus.COMPLETED;
    }

    public void cancelMatching() {
        status = WaitingQueueStatus.CANCELED;
    }

    public boolean isCompleted() {
        return status == WaitingQueueStatus.COMPLETED;
    }

    public boolean isMatching() {
        return status == WaitingQueueStatus.MATCHING;
    }

    public void leave(long memberId) {
        validateInMatching();
        WaitingQueueMember joinedWaitingQueueMember = getJoinedWaitingQueueMember(memberId);
        joinedWaitingQueueMember.leaveWaitingQueue();
        waitingQueueMembers.remove(joinedWaitingQueueMember);
        cancelWaitingQueueIfEmpty();
    }

    private void cancelWaitingQueueIfEmpty() {
        if (filterJoinedMembers().isEmpty()) {
            status = WaitingQueueStatus.CANCELED;
        }
    }

    private WaitingQueueMember getJoinedWaitingQueueMember(long memberId) {
        return waitingQueueMembers.stream()
                .filter(wqm -> wqm.getMember().getId() == memberId && wqm.isJoined())
                .findFirst()
                .orElseThrow(() -> new BizException(ErrorCode.NOT_IN_WAITING));
    }

    public int countJoinedMember() {
        return filterJoinedMembers().size();
    }

    private List<WaitingQueueMember> filterJoinedMembers() {
        return waitingQueueMembers.stream()
                .filter(wqm -> wqm.getStatus() == WaitingQueueMemberStatus.JOINED)
                .collect(Collectors.toList());
    }

    public void setId(Long id) {
        this.id = id;
    }
}
