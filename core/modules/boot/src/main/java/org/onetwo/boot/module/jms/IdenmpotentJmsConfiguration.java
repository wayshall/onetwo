package org.onetwo.boot.module.jms;

import org.onetwo.boot.mq.cosume.DbmReceiveMessageRepository;
import org.onetwo.boot.mq.cosume.ReceiveMessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
	
	@Bean
	public JmsConsumeMessageStoreService jmsConsumeMessageStoreService() {
		return new JmsConsumeMessageStoreService();
	}
	

	@Bean
	@ConditionalOnClass(name="org.onetwo.common.db.spi.BaseEntityManager")
	@ConditionalOnMissingBean(ReceiveMessageRepository.class)
	public ReceiveMessageRepository receiveMessageRepository() {
		return new DbmReceiveMessageRepository();
	}
	

}
