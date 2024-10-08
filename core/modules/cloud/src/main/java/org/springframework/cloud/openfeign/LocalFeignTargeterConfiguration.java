package org.springframework.cloud.openfeign;

import org.onetwo.cloud.feign.FeignProperties;
import org.onetwo.cloud.feign.FeignProperties.LocalProps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnClass(feign.Feign.class)
public class LocalFeignTargeterConfiguration {

	@Bean
	@ConditionalOnProperty(name = LocalProps.ENABLE_KEY, havingValue = "true")
	public Targeter feignTargeter() {
		return new LocalTargeter();
	}
	
	@Bean
	@ConditionalOnProperty(value=LocalProps.ENABLE_KEY)
	public TargeterEnhancer localTargeterEnhancer() {
		return new LocalTargeterEnhancer();
	}
	
	@Bean
	public LocalFeignTransactionWrapper localFeignTransactionWrapper() {
		return new LocalFeignTransactionWrapper();
	}
	/*@Bean
	@ConditionalOnMissingBean(TargeterEnhancer.class)
	public TargeterEnhancer targeterEnhancer() {
		return new PropsTargeterEnhancer();
	}*/
}
