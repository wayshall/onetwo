package org.onetwo.boot.module.redis;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author weishao zeng
 * <br/>
 */
public interface RedisOperationService {

	RedisLockRunner getRedisLockRunnerByKey(String key);

	<T> Optional<T> getCacheIfPreset(String key, Class<T> clazz);

	/***
	 * 根据key获取缓存，如果还不存在，则上锁执行cacheLoader
	 * @author weishao zeng
	 * @param key
	 * @param cacheLoader
	 * @return
	 */
	<T> T getCache(String key, Supplier<CacheData<T>> cacheLoader);

	String getAndDelString(String key);
	<T> T getAndDel(String key);
	
	Long clear(String key);

	RedisTemplate<Object, Object> getRedisTemplate();

	StringRedisTemplate getStringRedisTemplate();

	CacheStatis getCacheStatis();

}
