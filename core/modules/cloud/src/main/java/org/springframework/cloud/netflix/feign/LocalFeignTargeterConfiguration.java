package org.springframework.cloud.netflix.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class LocalFeignTargeterConfiguration {

	@Bean
	public Targeter feignTargeter() {
		return new LocalTargeter();
	}
}
