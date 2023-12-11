package org.onetwo.boot.module.jms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Import(IdenmpotentJmsListenerConfigurer.class)
public class IdenmpotentJmsConfiguration {
	
	@Bean
	public IdenmpotentJmsListenerConfigurer idenmpotentJmsListenerConfigurer() {
		return new IdenmpotentJmsListenerConfigurer();
	}
	
	@Bean
	public DelegateJmsMessageConsumer delegateJmsMessageConsumer() {
		return new DelegateJmsMessageConsumer();
	}

}
