package com.lof.waitingqueue.repository;

import static org.assertj.core.api.Assertions.assertThat;

import static com.lof.member.fixture.MemberFixture.VALID_LOGIN_ID;
import static com.lof.member.fixture.MemberFixture.createMember;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import com.lof.member.domain.Member;
import com.lof.member.repository.MemberRepository;
import com.lof.waitingqueue.domain.WaitingQueue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 테스트용 인메모리 DB가 아닌 MySQL에 실제 연결
class WaitingQueueRepositoryTest {

    @Autowired
    private WaitingQueueRepository queueRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Commit
    @Test
    @DisplayName("대기열에 속한 회원들의 평균점수가 lowerBound~upperBound 사이인 대기열 중 생성된 지 가장 오래된 대기열을 찾는다.")
    void findByAvgScoreBetween() {
        // given
        /*
        queue1 : 평균점수 1001점
        queue2 : 평균점수 1002점
        queue3 : 평균점수 1003점
         */
        Member member1 = createMember(VALID_LOGIN_ID + "1", 1);
        Member member2 = createMember(VALID_LOGIN_ID + "2", 1);
        Member member3 = createMember(VALID_LOGIN_ID + "3", 2);
        Member member4 = createMember(VALID_LOGIN_ID + "4", 2);
        Member member5 = createMember(VALID_LOGIN_ID + "5", 3);
        Member member6 = createMember(VALID_LOGIN_ID + "6", 3);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);

        WaitingQueue queue1 = new WaitingQueue();
        WaitingQueue queue2 = new WaitingQueue();
        WaitingQueue queue3 = new WaitingQueue();
        queueRepository.save(queue1);
        queueRepository.save(queue2);
        queueRepository.save(queue3);

        queue1.addMember(member1);
        queue1.addMember(member2);
        queue2.addMember(member3);
        queue2.addMember(member4);
        queue3.addMember(member5);
        queue3.addMember(member6);

        em.flush();

        // when
        WaitingQueue result = queueRepository.findByAvgScoreBetween(1002, 1003).get();

        // then
        assertThat(result).isEqualTo(queue2);
    }

    // 락을 제대로 획득하는지에 대한 테스트코드도 필요할까?
}
