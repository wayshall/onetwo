package org.onetwo.boot.module.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.data.redis.cache.RedisCacheManager;

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
	private List<CacheNamesProvider> providers;

	@Override
	public void customize(RedisCacheManager cacheManager) {
		Set<String> cacheNames = Sets.newHashSet();
		if(properties.getCacheNames()!=null){
			cacheNames.addAll(properties.getCacheNames());
		}
		cacheNames.addAll(getCacheNamesFromProviders());
		cacheNames.addAll(properties.getExpires().keySet());
		
		cacheManager.setCacheNames(ImmutableSet.copyOf(cacheNames));
		cacheManager.setDefaultExpiration(properties.getDefaultExpirationInSeconds());
		cacheManager.setLoadRemoteCachesOnStartup(properties.isLoadRemoteCachesOnStartup());
		cacheManager.setUsePrefix(properties.isUsePrefix());
		cacheManager.setTransactionAware(properties.isTransactionAware());
		
		Map<String, Long> expires = Maps.newHashMap();
//		expires.putAll(getCacheExpiresFromProviders());
		expires.putAll(properties.expiresInSeconds());
		cacheManager.setExpires(expires);
		//GenericJackson2JsonRedisSerializer
	}
	
	private Collection<String> getCacheNamesFromProviders(){
		if(providers==null){
			return Collections.emptyList();
		}
		return providers.stream().flatMap(d->d.cacheNames().stream()).collect(Collectors.toSet());
	}
	
	/*private Collection<String> getCacheNamesFromProviders(){
		return getCacheDataFromProviders().stream().flatMap(d->d.keySet().stream()).collect(Collectors.toSet());
	}

	
	private Map<String, Long> getCacheExpiresFromProviders(){
		return getCacheDataFromProviders().stream().reduce(Maps.newHashMap(), (p, v)->{
			p.putAll(v);
			return p;
		});
	}
	
	private Collection<Map<String, Long>> getCacheDataFromProviders(){
		if(providers==null){
			return Collections.emptyList();
		}
		return providers.stream().map(p->p.cacheNames()).collect(Collectors.toSet());
	}*/
	
}
