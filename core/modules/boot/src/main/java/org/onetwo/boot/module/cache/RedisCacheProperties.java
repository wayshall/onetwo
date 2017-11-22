package org.onetwo.boot.module.cache;

import java.util.Collection;
import java.util.Map;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties("jfish.cache.redis")
@Data
public class RedisCacheProperties {
	Collection<String> cacheNames;
	boolean usePrefix = false;
	boolean transactionAware = false;
	boolean loadRemoteCachesOnStartup = false;
	// 0 - never expire
	int defaultExpirationInSeconds = 0;
	
	Map<String, Long> expires = Maps.newHashMap(); 
}
