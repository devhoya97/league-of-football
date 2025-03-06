package com.lof.waitingqueue.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.lof.member.domain.Member;
import com.lof.member.fixture.MemberFixture;
import com.lof.member.repository.MemberRepository;
import com.lof.waitingqueue.domain.WaitingQueue;
import com.lof.waitingqueue.domain.WaitingQueueMember;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 테스트용 인메모리 DB가 아닌 MySQL에 실제 연결
class WaitingQueueMemberRepositoryTest {

    @Autowired
    private WaitingQueueRepository queueRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WaitingQueueMemberRepository queueMemberRepository;

    @Test
    @DisplayName("회원이 아직 매칭 중인 대기열에 참여하고 있으면 true를 반환한다.")
    void isInMatchingQueue() {
        // given
        Member member = MemberFixture.createMember(MemberFixture.USERNAME, MemberFixture.VALID_PASSWORD);
        memberRepository.save(member);
        LocalDate gameDate = LocalDate.now();
        WaitingQueue queue = new WaitingQueue(gameDate);
        queueRepository.save(queue);
        queueMemberRepository.save(new WaitingQueueMember(member, queue));

        // when
        boolean result = queueMemberRepository.isJoinedInMatchingQueue(member.getId(), gameDate);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("회원이 아직 매칭 중인 대기열에 참여하고 있지 않으면 false를 반환한다.")
    void isInMatchingQueueFalse() {
        // given
        Member member = MemberFixture.createMember(MemberFixture.USERNAME, MemberFixture.VALID_PASSWORD);
        memberRepository.save(member);
        LocalDate gameDate = LocalDate.now();
        WaitingQueue queue = new WaitingQueue(gameDate);
        queue.completeMatching();
        queueRepository.save(queue);
        queueMemberRepository.save(new WaitingQueueMember(member, queue));

        // when
        boolean result = queueMemberRepository.isJoinedInMatchingQueue(member.getId(), gameDate);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("회원이 특정 날짜에 아직 매칭 중인 대기열에 참여하고 있지 않으면 false를 반환한다.")
    void isInMatchingQueueFalseByDate() {
        // given
        Member member = MemberFixture.createMember(MemberFixture.USERNAME, MemberFixture.VALID_PASSWORD);
        memberRepository.save(member);
        LocalDate gameDate = LocalDate.now();
        WaitingQueue queue = new WaitingQueue(gameDate);
        queueRepository.save(queue);
        queueMemberRepository.save(new WaitingQueueMember(member, queue));

        // when
        boolean result = queueMemberRepository.isJoinedInMatchingQueue(member.getId(), gameDate.minusDays(1));

        // then
        assertThat(result).isFalse();
    }
}
