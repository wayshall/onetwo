package org.onetwo.common.spring.retry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class RetryTestContext {
	
	@Bean
	public RetryTestService retryTestService(){
		return new RetryTestService();
	}

}
