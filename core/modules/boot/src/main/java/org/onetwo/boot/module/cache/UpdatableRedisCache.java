package org.onetwo.boot.module.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
public class UpdatableRedisCache extends RedisCache {

	private final RedisOperations redisOperations;
	private ConcurrentMap<byte[], Long> updateTimestamps = Maps.newConcurrentMap();
//	private ConfigurablePropertyAccessor self;
	final private long expirationInSeconds;
	private RedisCacheManager redisCacheManager;

	public UpdatableRedisCache(
			String name,
			byte[] prefix,
			RedisOperations<? extends Object, ? extends Object> redisOperations,
			long expiration) {
		super(name, prefix, redisOperations, expiration);
		this.redisOperations = redisOperations;
//		self = PropertyAccessorFactory.forDirectFieldAccess(this);
		this.expirationInSeconds = expiration;
	}
	
	@Override
	public ValueWrapper get(Object key) {
		return super.get(key);//this.redisOperations.getExpire(key);
	}

	@Override
	public RedisCacheElement get(final RedisCacheKey cacheKey) {
		RedisCacheElement element =  super.get(cacheKey);
		if(element!=null){
			byte[] keyBytes = cacheKey.getKeyBytes();
			Long lasted = updateTimestamps.get(keyBytes);
			boolean checkCache = false;
			long current = System.currentTimeMillis();
			if(lasted!=null){
				long durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(current - lasted);
				if(durationInSeconds>3){
					checkCache = true;
					updateTimestamps.put(keyBytes, current);
				}
			}else{
				updateTimestamps.putIfAbsent(keyBytes, current);
			}
			if(checkCache){
				Long ttlInSeconds = this.redisOperations.getExpire(keyBytes);
				if(ttlInSeconds<=10){
					//update cache;
					this.evict(element);
				}
			}
		}
		return element;
	}

}
