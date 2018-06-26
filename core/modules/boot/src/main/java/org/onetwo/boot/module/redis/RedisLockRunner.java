package org.onetwo.boot.module.redis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.exception.BaseException;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class RedisLockRunner {
	
	final private RedisLockRegistry redisLockRegistry;
	private String lockKey;
	private Long time;
	private TimeUnit unit;
	private Consumer<Exception> errorHandler;

	@Builder
	public RedisLockRunner(RedisLockRegistry redisLockRegistry, String lockKey,
			Long time, TimeUnit unit, Consumer<Exception> errorHandler) {
		super();
		this.redisLockRegistry = redisLockRegistry;
		this.lockKey = lockKey;
		this.time = time;
		this.unit = unit;
		this.errorHandler = errorHandler;
	}
	
	public <T> T tryLock(Supplier<T> action){
		return tryLock(action, null);
	}
	
	public <T> T tryLock(Supplier<T> action, Supplier<T> lockFailAction){
		Function<Lock, Boolean> lockTryer = null;
		if(time!=null && unit!=null){
			lockTryer = lock->{
				try {
					return lock.tryLock(time, unit);
				} catch (InterruptedException e) {
					throw new BaseException("try lock error", e);
				}
			};
		}else{
			lockTryer = lock->lock.tryLock();
		}
		return tryLock(lockTryer, action, lockFailAction);
	}
	
	public <T> T tryLock(Function<Lock, Boolean> lockTryer, Supplier<T> action){
		return tryLock(lockTryer, action, null);
	}
	
	/***
	 * 
	 * @author wayshall
	 * @param lockTryer
	 * @param action 锁成功后执行
	 * @param lockFailAction 锁失败后执行
	 * @return
	 */
	public <T> T tryLock(Function<Lock, Boolean> lockTryer, Supplier<T> action, Supplier<T> lockFailAction){
		Lock lock = redisLockRegistry.obtain(lockKey);
		T result = null;
		try {
			if(!lockTryer.apply(lock)){
				if(lockFailAction!=null){
					return lockFailAction.get();
				}
				
				log.warn("can not obtain task lock, ignore task. lock key: {}", lockKey);
				return null;
			}
			if(log.isDebugEnabled()){
				log.debug("lock with key : {}", lockKey);
			}
			result = action.get();
		} catch (Exception e) {
			handleException(e);
		} finally{
			lock.unlock();
			if(log.isDebugEnabled()){
				log.debug("unlock with key : {}", lockKey);
			}
		}
		return result;
	}

	protected void handleException(Exception e){
		if(errorHandler!=null){
			errorHandler.accept(e);
		}else{
			log.error("execute error: "+e.getMessage(), e);
		}
	}

}

	