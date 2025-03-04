package com.lof.waitingqueue.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lof.waitingqueue.domain.WaitingQueue;

@Repository
public interface WaitingQueueRepository extends JpaRepository<WaitingQueue, Long> {

    // 여기서 List<WaitingQueueMember> 도 fetch join 해야겠는데? fetch join 안하면 list.add했을 때 어떻게되지?
    // 큐에 락 걸면 되긴 하겠다.
//    @Query(value = """
//            SELECT q.*
//            FROM waiting_queue q
//            JOIN waiting_queue_member m ON m.waiting_queue_id = q.id AND q.status = 'MATCHING'
//            GROUP BY q.id
//            HAVING AVG(m.rank_score) BETWEEN :lowerBound AND :upperBound
//            ORDER BY q.id
//            LIMIT 1
//            FOR UPDATE
//            """, nativeQuery = true)

    // TODO: 로우락 획득하기
    @Query("""
            SELECT wq FROM WaitingQueue wq
            JOIN wq.waitingQueueMembers wqm ON wq.status = 'MATCHING'
            JOIN wqm.member m
            GROUP BY wq.id
            HAVING AVG(m.rankScore) BETWEEN :lowerBound AND :upperBound
            ORDER BY wq.id
            """)
    List<WaitingQueue> findByAvgScoreBetween(@Param("lowerBound") int lowerBound, @Param("upperBound") int upperBound, Pageable pageable);
}
