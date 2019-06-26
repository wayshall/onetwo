package org.springframework.cloud.netflix.feign;

import org.onetwo.cloud.feign.FeignProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这个比较奇怪，必须放到spring.factories，@ConditionalOnProperty才起作用
 * @author wayshall
 * <br/>
 */
@Configuration
//@ConditionalOnProperty(value=FeignProperties.LOCAL_ENABLE_KEY, havingValue="true", matchIfMissing=false)
@EnableConfigurationProperties({FeignProperties.class})
public class LocalFeignTargeterConfiguration {

	@Bean
	public Targeter feignTargeter() {
		return new LocalTargeter();
	}
}
