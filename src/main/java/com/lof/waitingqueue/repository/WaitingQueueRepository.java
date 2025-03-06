package com.lof.waitingqueue.repository;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lof.waitingqueue.domain.WaitingQueue;

@Repository
public interface WaitingQueueRepository extends JpaRepository<WaitingQueue, Long> {

    // TODO: 비관적락이 정말 최선인지 더 고민해보기
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT wq FROM WaitingQueue wq
            JOIN wq.waitingQueueMembers wqm 
                ON wq.status = 'MATCHING' AND wq.gameDate = :gameDate AND wqm.status = 'JOINED'
            JOIN wqm.member m
            GROUP BY wq.id
            HAVING AVG(m.rankScore) BETWEEN :lowerBound AND :upperBound
            ORDER BY wq.id
            """)
    List<WaitingQueue> findWaitingQueueAvgScoreInRangeOrderByCreatedAt(@Param("lowerBound") int lowerBound,
                                                                       @Param("upperBound") int upperBound,
                                                                       @Param("gameDate") LocalDate gameDate,
                                                                       Pageable pageable);
}
