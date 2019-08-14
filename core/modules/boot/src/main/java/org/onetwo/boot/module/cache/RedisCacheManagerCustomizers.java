package org.onetwo.boot.module.cache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.onetwo.boot.module.redis.JsonRedisTemplate;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author wayshall
 * <br/>
 */
public class RedisCacheManagerCustomizers implements CacheManagerCustomizer<RedisCacheManager> {
	@Autowired
	private RedisCacheProperties properties;
	@Autowired(required=false)
	private List<CacheConfigProvider> providers;
	@Autowired 
	private JedisConnectionFactory jedisConnectionFactory;

	@Override
	public void customize(RedisCacheManager cacheManager) {
		Map<String, Long> cacheExpires = getCacheConfigsFromProviders();
		
		Set<String> cacheNames = Sets.newHashSet();
		if(properties.getCacheNames()!=null){
			cacheNames.addAll(properties.getCacheNames());
		}
		cacheNames.addAll(cacheExpires.keySet());
		// 配置文件覆盖
		cacheNames.addAll(properties.getExpires().keySet());
		
		cacheManager.setCacheNames(ImmutableSet.copyOf(cacheNames));
		cacheManager.setDefaultExpiration(properties.getDefaultExpirationInSeconds());
		cacheManager.setLoadRemoteCachesOnStartup(properties.isLoadRemoteCachesOnStartup());
		cacheManager.setUsePrefix(properties.isUsePrefix());
		cacheManager.setTransactionAware(properties.isTransactionAware());
		cacheManager.setCachePrefix(new ZifishRedisCachePrefix(properties.getCacheKeyPrefix()));
		
		Map<String, Long> expires = Maps.newHashMap();
//		expires.putAll(getCacheExpiresFromProviders());
		expires.putAll(cacheExpires);
		expires.putAll(properties.expiresInSeconds());
		cacheManager.setExpires(expires);
		
		if(properties.isUseJsonRedisTemplate()){
			JsonRedisTemplate template = new JsonRedisTemplate(jedisConnectionFactory);
			ReflectUtils.setFieldValue(cacheManager, "redisOperations", template);
		}
		//GenericJackson2JsonRedisSerializer
	}

	/***
	 * 获取通过代码配置的cache
	 * @author wayshall
	 * @return
	 */
	private Map<String, Long> getCacheConfigsFromProviders(){
		if(LangUtils.isEmpty(providers)){
			return Collections.emptyMap();
		}
		return providers.stream().flatMap(provider->{
			return provider.cacheConfigs().stream();
		}).collect(Collectors.toMap(config->config.getName(), config->{
			return config.getTimeUnit().toSeconds(config.getExpire());
		}));
	}
	
/*	private Collection<String> getCacheNamesFromProviders(){
		if(providers==null){
			return Collections.emptyList();
		}
		return providers.stream().flatMap(d->d.cacheNames().stream()).collect(Collectors.toSet());
	}
*/	
	/*private Collection<String> getCacheNamesFromProviders(){
		return getCacheDataFromProviders().stream().flatMap(d->d.keySet().stream()).collect(Collectors.toSet());
	}

	private Collection<Map<String, Long>> getCacheDataFromProviders(){
		if(providers==null){
			return Collections.emptyList();
		}
		return providers.stream().map(p->p.cacheNames()).collect(Collectors.toSet());
	}*/
	
}
