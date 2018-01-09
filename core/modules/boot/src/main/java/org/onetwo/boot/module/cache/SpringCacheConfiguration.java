package org.onetwo.boot.module.cache;

import org.onetwo.common.spring.cache.MethodKeyGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 如果检测到有缓存配置：spring.cache.type: xxx
 * 则启用自定义的默认key生成策略
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(name=SpringCacheConfiguration.SPRING_CACHE_ENABLED_KEY)
@EnableCaching
@Import(RedisCacheConfiguration.class)
@AutoConfigureAfter(CacheAutoConfiguration.class)
//@AutoConfigureBefore(CacheAutoConfiguration.class)
public class SpringCacheConfiguration {
	public static final String SPRING_CACHE_ENABLED_KEY = "spring.cache.type";
	
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
