package com.lof.waitingqueue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lof.waitingqueue.domain.WaitingQueue;

@Repository
public interface WaitingQueueRepository extends JpaRepository<WaitingQueue, Long> {

    @Query(value = """
            SELECT q.* 
            FROM waiting_queue q
            JOIN member m ON m.queue_id = q.id AND q.status = 'MATCHING'
            GROUP BY q.id
            HAVING AVG(m.rank_score) BETWEEN :lowerBound AND :upperBound
            ORDER BY q.id 
            LIMIT 1
            FOR UPDATE
            """, nativeQuery = true)
    Optional<WaitingQueue> findByAvgScoreBetween(@Param("lowerBound") int lowerBound, @Param("upperBound") int upperBound);
}
