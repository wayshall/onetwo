package org.onetwo.boot.module.redis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangOps;
import org.slf4j.Logger;
import org.springframework.integration.redis.util.RedisLockRegistry;

import lombok.Builder;

/**
 * 默认超时一分钟
 * 
 * @author wayshall
 * <br/>
 */
public class RedisLockRunner {
	
	/****
	 * 
	 * @deprecated 写错了单词……
	 * @author weishao zeng
	 * @param redisLockRegistry
	 * @param lockkey
	 * @param lockerTimeout
	 * @return
	 */
	@Deprecated
	public static RedisLockRunner createLoker(RedisLockRegistry redisLockRegistry, String lockkey, String lockerTimeout) {
		return createLocker(redisLockRegistry, lockkey, lockerTimeout);
	}
	
	public static RedisLockRunner createLocker(RedisLockRegistry redisLockRegistry, String lockkey, String lockerTimeout) {
		Pair<Integer, TimeUnit> timeout = null;
		if (StringUtils.isNotBlank(lockerTimeout)) {
			timeout = LangOps.parseTimeUnit(lockerTimeout);
		}
		RedisLockRunner redisLockRunner = RedisLockRunner.builder()
														 .lockKey(lockkey)
														 .time(timeout==null?null:timeout.getKey().longValue())
														 .unit(timeout==null?null:timeout.getValue())
														 .errorHandler(e->{
															 if (e instanceof RuntimeException) {
																 throw (RuntimeException) e;
															 }
															 throw new BaseException("execute task in redislock error!", e);
														 })
														 .redisLockRegistry(redisLockRegistry)
														 .build();
		return redisLockRunner;
	}
	
	// org.onetwo.boot.module.redis.RedisLockRunner
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	final private RedisLockRegistry redisLockRegistry;
	private String lockKey;
	/***
	 * 默认超时一分钟
	 */
	private Long time = 60L;
	private TimeUnit unit = TimeUnit.SECONDS;
	private Consumer<Exception> errorHandler;

	@Builder
	public RedisLockRunner(RedisLockRegistry redisLockRegistry, String lockKey,
			Long time, TimeUnit unit, Consumer<Exception> errorHandler) {
		super();
		this.redisLockRegistry = redisLockRegistry;
		this.lockKey = lockKey;
		if (time!=null) {
			this.time = time;
		}
		if (unit!=null) {
			this.unit = unit;
		}
		this.errorHandler = errorHandler;
	}
	
	public <T> T tryLock(Supplier<T> action){
		return tryLock(action, null);
	}
	
	public <T> T tryLock(Supplier<T> action, Supplier<T> lockFailAction){
		/*Function<Lock, Boolean> lockTryer = null;
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
		return tryLock(lockTryer, action, lockFailAction);*/
		return tryLock(time, unit, action, lockFailAction);
	}
	
	public <T> T tryLock(Function<Lock, Boolean> lockTryer, Supplier<T> action){
		return tryLock(lockTryer, action, null);
	}
	

	public <T> T tryLock(Long time, TimeUnit unit, Supplier<T> action, Supplier<T> lockFailAction){
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
		boolean locked = false;
		try {
			locked = lockTryer.apply(lock);
			if(!locked){
				if(lockFailAction!=null){
					if(logger.isDebugEnabled()){
						logger.debug("try lock failed with key : {}. execute lockFailAction", lockKey);
					}
					return lockFailAction.get();
				}
				
				logger.warn("can not obtain redis lock, ignore operation. lock key: {}", lockKey);
				return null;
			}
			if(logger.isDebugEnabled()){
				logger.debug("try lock success with key : {}, execute lock action", lockKey);
			}
			result = action.get();
		} catch (Exception e) {
			handleException(e);
		} finally{
			if (locked) {
				lock.unlock();
				if(logger.isDebugEnabled()){
					logger.debug("unlock with key : {}", lockKey);
				}
			}
		}
		return result;
	}

	public <T> T lock(Supplier<T> action){
		Lock lock = redisLockRegistry.obtain(lockKey);
		T result = null;
		try {
			if(logger.isDebugEnabled()){
				logger.debug("lock with key : {}", lockKey);
			}
			lock.lock();
			if(logger.isDebugEnabled()){
				logger.debug("locked with key : {}", lockKey);
			}
			result = action.get();
		} catch (Exception e) {
			handleException(e);
		} finally{
			lock.unlock();
			if(logger.isDebugEnabled()){
				logger.debug("unlock with key : {}", lockKey);
			}
		}
		return result;
	}
	protected void handleException(Exception e){
		if(errorHandler!=null){
			errorHandler.accept(e);
		}else{
			logger.error("execute error: "+e.getMessage(), e);
		}
	}

}

	