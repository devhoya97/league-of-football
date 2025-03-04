package com.lof.waitingqueue.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.lof.global.BaseEntity;
import com.lof.global.exception.ErrorCode;
import com.lof.global.exception.ServerLoginException;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO: 평균점수는 Member 테이블과 join해서 구하는데, 이 성능이 느리다면 여기서 필드로 저장하는 방법 고려
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class WaitingQueue extends BaseEntity {

    private static final int MAX_SIZE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "waitingQueue")
    private List<WaitingQueueMember> waitingQueueMembers = new ArrayList<>(); // WaitingQueueMemberStatus == 'LEAVE'인 애들은 여기 포함 안시키고 싶은데... JPA가 그건 못하지 않나?

    @Enumerated(EnumType.STRING)
    private WaitingQueueStatus status = WaitingQueueStatus.MATCHING;

    public void addWaitingQueueMember(WaitingQueueMember queueMember) {
//        validateQueueSize();

        waitingQueueMembers.add(queueMember);
        if (filterWaitingMembers().size() >= MAX_SIZE) {
            this.status = WaitingQueueStatus.COMPLETED;
        }
    }

    public void completeMatching() {
        status = WaitingQueueStatus.COMPLETED;
    }

    public void cancelMatching() {
        status = WaitingQueueStatus.CANCELED;
    }

    public boolean hasOnlyOneWaitingMember() {
        return filterWaitingMembers().size() == 1;
    }

    private List<WaitingQueueMember> filterWaitingMembers() {
        return waitingQueueMembers.stream()
                .filter(wqm -> wqm.getWaitingQueueMemberStatus() == WaitingQueueMemberStatus.WAITING)
                .toList();
    }
}
