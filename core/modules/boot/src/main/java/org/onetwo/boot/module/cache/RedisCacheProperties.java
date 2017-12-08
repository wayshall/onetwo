package org.onetwo.boot.module.cache;

import java.util.Collection;
import java.util.Map;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

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
	boolean usePrefix = false;
	boolean transactionAware = false;
	boolean loadRemoteCachesOnStartup = false;
	// 0 - never expire
	int defaultExpirationInSeconds = 0;
	
	Map<String, Long> expires = Maps.newHashMap(); 
}
