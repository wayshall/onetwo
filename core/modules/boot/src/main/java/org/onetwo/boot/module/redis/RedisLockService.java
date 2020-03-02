package org.onetwo.boot.module.redis;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

import org.onetwo.common.exception.BaseException;
import org.springframework.integration.redis.util.RedisLockRegistry;

public class RedisLockService {
	
	private RedisLockRegistry redisLockRegistry;
	
	public RedisLockService(RedisLockRegistry redisLockRegistry) {
		super();
		this.redisLockRegistry = redisLockRegistry;
	}

	public void tryLock(String lockKey, long timeInMillis, Runnable action) {
		Lock locker = redisLockRegistry.obtain(lockKey);
		try {
			locker.tryLock(timeInMillis, TimeUnit.MILLISECONDS);
			action.run();
		} catch (InterruptedException e) {
			throw new BaseException("lock[" + lockKey + "] has interrupted!");
		} finally {
			locker.unlock();
		}
	}
	
	public <T> T lock(String lockKey, long timeInMillis, Supplier<T> action) {
		Lock locker = redisLockRegistry.obtain(lockKey);
		try {
			locker.tryLock(timeInMillis, TimeUnit.MILLISECONDS);
			return action.get();
		} catch (InterruptedException e) {
			throw new BaseException("lock[" + lockKey + "] has interrupted!");
		} finally {
			locker.unlock();
		}
	}
	
	public void lock(String lockKey, Runnable action) {
		Lock locker = redisLockRegistry.obtain(lockKey);
		try {
			locker.lock();
			action.run();
		} finally {
			locker.unlock();
		}
	}
	
	public <T> T lock(String lockKey, Supplier<T> action) {
		Lock locker = redisLockRegistry.obtain(lockKey);
		try {
			locker.lock();
			return action.get();
		} finally {
			locker.unlock();
		}
	}

}
