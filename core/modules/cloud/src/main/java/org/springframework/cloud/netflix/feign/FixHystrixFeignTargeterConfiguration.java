package org.springframework.cloud.netflix.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(name = "feign.hystrix.HystrixFeign")
public class FixHystrixFeignTargeterConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Targeter feignTargeter() {
		return new EnhanceHystrixTargeter();
	}
}
