package com.lof.waitingqueue.repository;

import static org.assertj.core.api.Assertions.assertThat;

import static com.lof.member.fixture.MemberFixture.USERNAME;
import static com.lof.member.fixture.MemberFixture.createMember;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;

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

    @Autowired
    private EntityManager em;

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
        LocalDate gameDate = LocalDate.now();
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

        WaitingQueue queue1 = new WaitingQueue(gameDate);
        WaitingQueue queue2 = new WaitingQueue(gameDate);
        WaitingQueue queue3 = new WaitingQueue(gameDate);
        WaitingQueue queue4 = new WaitingQueue(gameDate);
        queueRepository.save(queue1);
        queueRepository.save(queue2);
        queueRepository.save(queue3);
        queueRepository.save(queue4);

        queue1.joinMember(member1);
        queue1.joinMember(member2);
        queue2.joinMember(member3);
        queue2.joinMember(member4);
        queue3.joinMember(member5);
        queue3.joinMember(member6);
        queue4.joinMember(member7);
        queue4.joinMember(member8);

        queue2.completeMatching();

        em.flush();

        // when
        List<WaitingQueue> result = queueRepository.findWaitingQueueAvgScoreInRangeOrderByCreatedAt(1002, 1004, gameDate, PageRequest.of(0, 1));

        // then
        assertThat(result).containsOnly(queue3);
        assertThat(result.get(0).getWaitingQueueMembers().stream().map(wqm -> wqm.getMember())).containsExactlyInAnyOrder(member5, member6);
    }

    // 락을 제대로 획득하는지에 대한 테스트코드도 필요할까?
}
