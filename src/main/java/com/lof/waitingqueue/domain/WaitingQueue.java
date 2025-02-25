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
import com.lof.member.domain.Member;

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

    @OneToMany(mappedBy = "queue")
    private List<Member> members = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private WaitingQueueStatus status = WaitingQueueStatus.MATCHING;

    public void addMember(Member member) {
        validateStatus();
        member.joinInWaitingQueue(this);
        members.add(member);

        if (members.size() == MAX_SIZE) {
            status = WaitingQueueStatus.COMPLETED;
        }
    }

    // 1. 이런 상황도 Validator에서 검증을 수행해야할까??
    // 2. 애초에 repository에서 들고올 때 MATCHING 상태인 애들만 들고오긴 하는데, 여기서 또 검증할 필요가 있을까? 객체지향 관점에서 보면 있는 것 같은데 괜히 오바하는 것 같기도 해서
    private void validateStatus() {
        if (status == WaitingQueueStatus.COMPLETED) {
            throw new ServerLoginException(ErrorCode.ALREADY_COMPLETED_WAITING_QUEUE);
        }
    }
}
