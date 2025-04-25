package com.lof.waitingqueue.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lof.common.District;
import com.lof.game.implement.GameManager;
import com.lof.global.DistributedLockExecutor;
import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;
import com.lof.member.domain.RankTier;
import com.lof.member.implement.MemberManager;
import com.lof.waitingqueue.repository.WaitingQueueRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private static final int LAST_MEMBER_NEEDED_QUEUE_SIZE = 9;
    private static final int FULL_MEMBER_QUEUE_SIZE = 10;
    private static final int ONLY_ONE_MEMBER_LEFT = 1;

    private final WaitingQueueRedisRepository waitingQueueRepository;
    private final MemberManager memberManager;
    private final GameManager gameManager;
    private final DistributedLockExecutor distributedLockExecutor;

    @Transactional
    public WaitingQueueStatus join(long memberId, LocalDate date, District district) {
        Member member = memberManager.getMemberById(memberId);
        RankTier rankTier = RankTier.findByRankScore(member.getRankScore());

        return distributedLockExecutor.execute(getLockKey(date, rankTier, district), 4, 3, () -> {
            Set<Long> memberIds = waitingQueueRepository.findMembers(date, rankTier, district);
            if (memberIds.contains(memberId)) {
                throw new BizException(ErrorCode.ALREADY_WAITING);
            }
            // 첫 회원
            if (memberIds.isEmpty()) {
                long gameId = gameManager.createGame(date, rankTier, district); // Q: 만약 MSA로 Game과 WaitingQueue가 분리되어 있다면, Game을 생성해달라는 API를 호출해야 하나? 비동기?
                waitingQueueRepository.joinMember(date, rankTier, district, memberId); // db에 게임 생성하는 것과 redis에 대기열 생성하는 순서는 어떻게 하는게 좋을까? 일단 db는 롤백이 쉬우니까 db를 앞에 뒀음
                waitingQueueRepository.saveGameId(date, rankTier, district, gameId); // 여기서 실패하면 redis에 joinMember한 것도 삭제해줘야하는데... try catch로 일일이 예외 잡아서 복구시켜줘야하나? 그럴거면 Lua스크립트 쓰는게 맞지 않나?
                return new WaitingQueueStatus(1, gameId);
            }
            Long gameId = waitingQueueRepository.findGameId(date, rankTier, district) // 검증 로직 어떻게 분리할까? 일단 서비스에 때려박고 수헌이형이 어떻게 책임분리하는지 보고 배우고 싶음.
                    .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_GAME));
            // 마지막 회원
            if (memberIds.size() == LAST_MEMBER_NEEDED_QUEUE_SIZE) {
                memberIds.add(memberId);
                gameManager.startGame(gameId, memberIds); // MSA면 여기도 Game API 호출하는 방식으로 바뀔 듯?
                waitingQueueRepository.deleteQueue(date, rankTier, district); // 여기도 마찬가지로 Lua 스크립트 적용해야 할지 고민
                waitingQueueRepository.deleteGameId(date, rankTier, district);
                return new WaitingQueueStatus(FULL_MEMBER_QUEUE_SIZE, gameId);
            }
            // 이미 생성된 게임에 참여하되, 마지막 회원은 아닌 경우
            waitingQueueRepository.joinMember(date, rankTier, district, memberId);
            return new WaitingQueueStatus(memberIds.size() + 1, gameId);
        });
    }

    @Transactional
    public void leave(LocalDate date, RankTier rankTier, District district, long memberId) {
        distributedLockExecutor.execute(getLockKey(date, rankTier, district), 4, 3, () -> {
            Set<Long> members = waitingQueueRepository.findMembers(date, rankTier, district);
            if (!members.contains(memberId)) {
                throw new BizException(ErrorCode.NOT_IN_WAITING);
            }
            if (members.size() == ONLY_ONE_MEMBER_LEFT) {
                long gameId = waitingQueueRepository.findGameId(date, rankTier, district)
                        .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_GAME));
                gameManager.deleteGame(gameId); // join에서와 같은 맥락으로 db에서 삭제하는 로직을 redis 삭제 로직보다 먼저 배치했음
                waitingQueueRepository.deleteGameId(date, rankTier, district); // join과 같은 맥락에서 lua 스크립트 고려 중
            }
            waitingQueueRepository.leaveMember(date, rankTier, district, memberId);
        });
    }

    // TODO: 유저가 새로고침 할 때마다 이 메서드는 필요하므로 냅두고, 게임이 생성될 때 서버 -> 클라이언트로 메시지 전달하는 기능 추가하기
    public int checkWaitingQueueSize(LocalDate date, RankTier rankTier, District district) {
        Set<Long> members = waitingQueueRepository.findMembers(date, rankTier, district);

        return members.size(); // 자신이 참여중인 대기열이 아니더라도 검색 가능
    }

    private String getLockKey(LocalDate date, RankTier rankTier, District district) {
        return date.format(DateTimeFormatter.BASIC_ISO_DATE) + ":" +
                rankTier.name() + ":" +
                district.name() + ":LOCK";
    }
    //TODO: 하루가 바뀔 때마다 redis에서 기간이 지난 대기열, gameId 저장한거 삭제하고, 해당 gameId들을 바탕으로 DB에 미리 만들어둔 game도 삭제하는 Batch 로직 구현하기
}
