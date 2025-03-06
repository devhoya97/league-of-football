package com.lof.waitingqueue.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lof.waitingqueue.domain.WaitingQueueMember;

@Repository
public interface WaitingQueueMemberRepository extends JpaRepository<WaitingQueueMember, Long> {

    @Query("""
            SELECT COUNT(wqm) > 0 FROM WaitingQueueMember wqm
            JOIN wqm.waitingQueue wq ON wq.status = 'MATCHING'
                AND wq.gameDate = :gameDate
                AND wqm.status = 'JOINED'
                AND wqm.member.id = :memberId
            """)
    boolean isJoinedInMatchingQueue(@Param("memberId") long memberId, @Param("gameDate")LocalDate gameDate);
}
