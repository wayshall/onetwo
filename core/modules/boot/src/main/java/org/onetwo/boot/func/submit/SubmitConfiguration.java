package org.onetwo.boot.func.submit;

import org.onetwo.boot.module.redis.JFishRedisProperties;
import org.onetwo.boot.module.redis.RedisOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@ConditionalOnProperty(name=JFishRedisProperties.ENABLED_KEY, havingValue="true", matchIfMissing=true)
public class SubmitConfiguration {
	
	@Bean
	public SubmitTokenService submitTokenService() {
		return new SubmitTokenService();
	}
	@Bean
	public SubmitTokenInterceptor submitTokenInterceptor() {
		return new SubmitTokenInterceptor();
	}
	
	@Bean
	@Autowired
	public RedisRateLimiter simpleRedisRateLimiter(RedisOperationService redisOperationService) {
		return new SimpleRedisRateLimiter(redisOperationService);
	}

}

