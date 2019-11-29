package org.onetwo.boot.func.submit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
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
	public SimpleFrequentlySubmitChecker simpleFrequentlySubmitChecker() {
		return new SimpleFrequentlySubmitChecker();
	}

}

