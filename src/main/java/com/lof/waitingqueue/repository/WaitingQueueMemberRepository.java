package com.lof.waitingqueue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lof.waitingqueue.domain.WaitingQueueMember;

@Repository
public interface WaitingQueueMemberRepository extends JpaRepository<WaitingQueueMember, Long> {

    @Query("""
            SELECT wqm FROM WaitingQueueMember wqm
            WHERE wqm.member.id = :memberId AND wqm.waitingQueueMemberStatus = 'WAITING'
            """)
    WaitingQueueMember findWaitingMember(@Param("memberId") long memberId);

    @Query("""
            SELECT COUNT(wqm) > 0 FROM WaitingQueueMember wqm
            JOIN wqm.waitingQueue wq ON wq.status = 'MATCHING' AND wqm.waitingQueueMemberStatus = 'WAITING' AND wqm.member.id = :memberId
            """)
    boolean isInMatchingQueue(@Param("memberId") long memberId);
}
