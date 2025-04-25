package com.lof.waitingqueue.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.lof.common.District;
import com.lof.member.domain.RankTier;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveGameId(LocalDate date, RankTier rankTier, District district, long gameId) {
        String gameKey = getGameKey(date, rankTier, district);
        redisTemplate.opsForValue().set(gameKey, String.valueOf(gameId));
    }

    public Optional<Long> findGameId(LocalDate date, RankTier rankTier, District district) {
        String gameKey = getGameKey(date, rankTier, district);
        Object rawGameId = redisTemplate.opsForValue().get(gameKey);

        if (rawGameId == null) {
            return Optional.empty();
        }
        return Optional.of(Long.parseLong(rawGameId.toString()));
    }

    private String getGameKey(LocalDate date, RankTier rankTier, District district) {
        return date.format(DateTimeFormatter.BASIC_ISO_DATE) + ":" +
                rankTier.name() + ":" +
                district.name() + ":GAME";
    }

    public boolean deleteGameId(LocalDate date, RankTier rankTier, District district) {
        String gameKey = getGameKey(date, rankTier, district);
        return redisTemplate.delete(gameKey);
    }

    public boolean joinMember(LocalDate date, RankTier rankTier, District district, long memberId) {
        String queueKey = getQueueKey(date, rankTier, district);
        Long addCount = redisTemplate.opsForSet().add(queueKey, String.valueOf(memberId));

        return addCount != null && addCount > 0;
    }

    public Set<Long> findMembers(LocalDate date, RankTier rankTier, District district) {
        String queueKey = getQueueKey(date, rankTier, district);
        Set<Object> rawMembers = redisTemplate.opsForSet().members(queueKey);

        if (rawMembers == null) {
            return Collections.emptySet();
        }

        return rawMembers.stream()
                .map(obj -> Long.parseLong(obj.toString()))
                .collect(Collectors.toSet());
    }

    // 회원이 다 나가면 key 자체를 삭제시켜버리는게 나을까? 아니면 TTL을 설정해주는게 나을까?
    public boolean leaveMember(LocalDate date, RankTier rankTier, District district, long memberId) {
        String queueKey = getQueueKey(date, rankTier, district);
        Set<Long> members = findMembers(date, rankTier, district);
        if (!members.contains(memberId)) {
            return false;
        }
        if (members.size() == 1) {
            return redisTemplate.delete(queueKey);
        }
        Long removed = redisTemplate.opsForSet().remove(queueKey, String.valueOf(memberId));
        return removed != null && removed > 0;
    }

    public boolean deleteQueue(LocalDate date, RankTier rankTier, District district) {
        String queueKey = getQueueKey(date, rankTier, district);
        return redisTemplate.delete(queueKey);
    }

    private String getQueueKey(LocalDate date, RankTier rankTier, District district) {
        return date.format(DateTimeFormatter.BASIC_ISO_DATE) + ":" +
                rankTier.name() + ":" +
                district.name() + ":MEMBERS";
    }
}
