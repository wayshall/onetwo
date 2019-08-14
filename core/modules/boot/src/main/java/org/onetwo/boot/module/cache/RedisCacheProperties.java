package org.onetwo.boot.module.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.boot.module.redis.RedisOperationService;
import org.onetwo.common.utils.LangOps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

import lombok.Data;

/**
 * spring: 
    cache: 
        type: redis
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(RedisCacheProperties.PREFIX)
@Data
public class RedisCacheProperties {
//	public static final String SPRING_CACHE_ENABLED_KEY = "spring.cache.type";
	
	public static final String PREFIX = "jfish.cache.redis";
	
	Collection<String> cacheNames;
	boolean usePrefix = true;
	boolean transactionAware = false;
	boolean loadRemoteCachesOnStartup = false;
	// 0 - never expire
	int defaultExpirationInSeconds = 0;
	
	Map<String, String> expires = Maps.newHashMap();
	
	boolean useJsonRedisTemplate = true;
	
	String cacheKeyPrefix = RedisOperationService.DEFAUTL_CACHE_PREFIX;
	
	public Map<String, Long> expiresInSeconds(){
		if(expires.isEmpty()){
			return Collections.emptyMap();
		}
		Map<String, Long> expiresInSeconds = Maps.newHashMapWithExpectedSize(expires.size());
		for(Entry<String, String> entry : expires.entrySet()){
			expiresInSeconds.put(entry.getKey(), LangOps.parseTime(entry.getValue(), 0, (d, tu)->tu.toSeconds(d)));
		}
		return expiresInSeconds;
	}
}
