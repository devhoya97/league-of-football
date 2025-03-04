package com.lof.waitingqueue.repository;

import static org.assertj.core.api.Assertions.assertThat;

import static com.lof.member.fixture.MemberFixture.USERNAME;
import static com.lof.member.fixture.MemberFixture.createMember;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import com.lof.member.domain.Member;
import com.lof.member.repository.MemberRepository;
import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.domain.WaitingQueueMember;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 테스트용 인메모리 DB가 아닌 MySQL에 실제 연결
class WaitingQueueRepositoryTest {

    @Autowired
    private WaitingQueueRepository queueRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WaitingQueueMemberRepository queueMemberRepository;

    @Test
    @DisplayName("대기열에 속한 회원들의 평균점수가 lowerBound~upperBound 사이인 대기열 중 생성된 지 가장 오래된 대기열을 찾는다.")
    void findByAvgScoreBetween() {
        // given
        /*
        queue1 : 평균점수 1001점, status = 'MATCHING'
        queue2 : 평균점수 1002점, status = 'COMPLETE'
        queue3 : 평균점수 1003점, status = 'MATCHING'
        queue4 : 평균점수 1004점, status = 'MATCHING'
         */
        Member member1 = createMember(USERNAME + "1", 1);
        Member member2 = createMember(USERNAME + "2", 1);
        Member member3 = createMember(USERNAME + "3", 2);
        Member member4 = createMember(USERNAME + "4", 2);
        Member member5 = createMember(USERNAME + "5", 3);
        Member member6 = createMember(USERNAME + "6", 3);
        Member member7 = createMember(USERNAME + "7", 4);
        Member member8 = createMember(USERNAME + "8", 4);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);
        memberRepository.save(member7);
        memberRepository.save(member8);

        WaitingQueue queue1 = new WaitingQueue();
        WaitingQueue queue2 = new WaitingQueue();
        WaitingQueue queue3 = new WaitingQueue();
        WaitingQueue queue4 = new WaitingQueue();
        queueRepository.save(queue1);
        queueRepository.save(queue2);
        queueRepository.save(queue3);
        queueRepository.save(queue4);

        queue2.completeMatching();

        queueMemberRepository.save(new WaitingQueueMember(member1, queue1));
        queueMemberRepository.save(new WaitingQueueMember(member2, queue1));
        queueMemberRepository.save(new WaitingQueueMember(member3, queue2));
        queueMemberRepository.save(new WaitingQueueMember(member4, queue2));
        queueMemberRepository.save(new WaitingQueueMember(member5, queue3));
        queueMemberRepository.save(new WaitingQueueMember(member6, queue3));
        queueMemberRepository.save(new WaitingQueueMember(member7, queue4));
        queueMemberRepository.save(new WaitingQueueMember(member8, queue4));

        // when
        List<WaitingQueue> result = queueRepository.findByAvgScoreBetween(1002, 1004, PageRequest.of(0, 1));

        // then
        assertThat(result).containsOnly(queue3);
    }

    // 락을 제대로 획득하는지에 대한 테스트코드도 필요할까?
}
