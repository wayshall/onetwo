package org.onetwo.boot.module.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

/**
 * 当检测到spring.cache.type为redis时，根据jfish.cache.redis的属性配置定制RedisCacheManager
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(name=SpringCacheConfiguration.SPRING_CACHE_ENABLED_KEY, havingValue="redis")
@ConditionalOnClass(RedisCacheManager.class)
@EnableConfigurationProperties(RedisCacheProperties.class)
public class RedisCacheConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(RedisCacheManager.class)
	public RedisCacheManagerCustomizers redisCacheManagerCustomizers(){
		return new RedisCacheManagerCustomizers();
	}
}
