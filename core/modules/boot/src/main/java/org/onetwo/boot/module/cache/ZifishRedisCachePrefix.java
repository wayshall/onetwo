package org.onetwo.boot.module.cache;

import org.onetwo.boot.module.redis.RedisOperationService;
import org.onetwo.common.utils.StringUtils;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author weishao zeng
 * <br/>
 */
public class ZifishRedisCachePrefix implements CacheKeyPrefix {
	public static String DEFAUTL_DELIMITER = ":";

	private final RedisSerializer<String> serializer = new StringRedisSerializer();
	
	private final String cacheDelimiter;
	private String cacheKeyPrefix = RedisOperationService.DEFAUTL_CACHE_PREFIX;

	public ZifishRedisCachePrefix() {
		this(DEFAUTL_DELIMITER, RedisOperationService.DEFAUTL_CACHE_PREFIX);
	}
	
	public ZifishRedisCachePrefix(String cacheKeyPrefix) {
		this(DEFAUTL_DELIMITER, cacheKeyPrefix);
	}
	
	public ZifishRedisCachePrefix(String delimiter, String cacheKeyPrefix) {
		this.cacheDelimiter = delimiter!=null?delimiter:DEFAUTL_DELIMITER;
		this.cacheKeyPrefix = cacheKeyPrefix!=null?cacheKeyPrefix:"";
	}
	
	@Override
	public String compute(String cacheName) {
		String cacheNameWithPrefix = StringUtils.appendEndWith(cacheKeyPrefix, cacheDelimiter).concat(cacheName);
		return cacheNameWithPrefix;
	}

	@Deprecated
	public byte[] prefix(String cacheName) {
		cacheName = StringUtils.appendEndWith(cacheKeyPrefix, cacheDelimiter).concat(cacheName);
		return serialize(cacheName);
	}

	@Deprecated
	protected byte[] serialize(String cacheName) {
		return serializer.serialize((cacheDelimiter != null ? cacheName.concat(cacheDelimiter) : cacheName.concat(DEFAUTL_DELIMITER)));
	}
}
