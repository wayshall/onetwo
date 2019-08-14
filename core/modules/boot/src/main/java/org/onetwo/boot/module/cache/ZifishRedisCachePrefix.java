package org.onetwo.boot.module.cache;

import org.onetwo.boot.module.redis.RedisOperationService;
import org.onetwo.common.utils.StringUtils;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;

/**
 * @author weishao zeng
 * <br/>
 */
public class ZifishRedisCachePrefix extends DefaultRedisCachePrefix {


	private final String cacheDelimiter;
	private String cacheKeyPrefix = RedisOperationService.DEFAUTL_CACHE_PREFIX;

	public ZifishRedisCachePrefix() {
		this(":", RedisOperationService.DEFAUTL_CACHE_PREFIX);
	}
	
	public ZifishRedisCachePrefix(String cacheKeyPrefix) {
		this(":", cacheKeyPrefix);
	}
	
	public ZifishRedisCachePrefix(String delimiter, String cacheKeyPrefix) {
		super(delimiter);
		this.cacheDelimiter = delimiter!=null?delimiter:":";
		this.cacheKeyPrefix = cacheKeyPrefix!=null?cacheKeyPrefix:"";
	}
	
	public byte[] prefix(String cacheName) {
		cacheName = StringUtils.appendEndWith(cacheKeyPrefix, cacheDelimiter).concat(cacheName);
		return super.prefix(cacheName);
	}
}
