package com.lof.global;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DistributedLockExecutor {

    private final RedissonClient redissonClient;

    public <T> T execute(String key, long waitSec, long leaseSec, Supplier<T> action) {
        RLock lock = redissonClient.getLock(key);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(waitSec, leaseSec, TimeUnit.SECONDS);
            if (!acquired) {
                throw new BizException(ErrorCode.LOCK_WAITING);
            }
            return action.get();
        } catch (InterruptedException e) {
            throw new IllegalStateException("분산 락 획득하여 작업 중에 인터럽트 발생", e);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void execute(String key, long waitSec, long leaseSec, Runnable action) {
        RLock lock = redissonClient.getLock(key);
        boolean acquired = false;

        try {
            acquired = lock.tryLock(waitSec, leaseSec, TimeUnit.SECONDS);
            if (!acquired) {
                throw new BizException(ErrorCode.LOCK_WAITING);
            }
            action.run();
        } catch (InterruptedException e) {
            throw new IllegalStateException("분산 락 획득 중 인터럽트 발생", e);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
