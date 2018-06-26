package org.springframework.cloud.netflix.feign;

import org.onetwo.cloud.feign.FeignProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(value=FeignProperties.LOCAL_ENABLE_KEY, havingValue="true", matchIfMissing=true)
public class LocalFeignTargeterConfiguration {

	@Bean
	public Targeter feignTargeter() {
		return new LocalTargeter();
	}
}
