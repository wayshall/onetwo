package org.onetwo.boot.module.cache;

import org.onetwo.common.spring.cache.MethodKeyGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(RedisCacheManager.class)
//@ConditionalOnBean(RedisCacheManager.class)
@EnableConfigurationProperties(RedisCacheProperties.class)
@EnableCaching
@AutoConfigureAfter(RedisCacheConfiguration.class)
public class RedisCacheConfiguration {
	
	@Bean
	public RedisCacheManagerCustomizers redisCacheManagerCustomizers(){
		return new RedisCacheManagerCustomizers();
	}
	
	@Bean
	public CachingConfigurer simpleCachingConfigurer(){
		return new SimpleCachingConfigurer();
	}

	/***
	 * AbstractCachingConfiguration#useCachingConfigurer
	 * @author wayshall
	 *
	 */
	public class SimpleCachingConfigurer extends CachingConfigurerSupport {

		@Override
		public KeyGenerator keyGenerator() {
			return new MethodKeyGenerator();
		}
		
	}
}
