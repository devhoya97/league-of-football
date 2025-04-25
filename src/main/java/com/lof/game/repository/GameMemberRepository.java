package com.lof.game.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lof.game.domain.GameMember;

@Repository
public interface GameMemberRepository extends JpaRepository<GameMember, Long> {

    Optional<GameMember> findByGameIdAndMemberId(long gameId, long memberId);

    List<GameMember> findAllByGameId(long gameId);
}
