package org.onetwo.boot.module.jms;

import org.onetwo.boot.mq.cosume.DbmReceiveMessageRepository;
import org.onetwo.boot.mq.cosume.ReceiveMessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Import(IdenmpotentJmsListenerConfigurer.class)
@EnableConfigurationProperties(JmsProperties.class)
public class JmsConfiguration {
	
	@Bean
//	@ConditionalOnProperty(name = JmsProperties.CONVERTER_KEY, havingValue = "jackson2", matchIfMissing = false)
	static MessageConverterConfigurater messageConverterConfigurater() {
		return new MessageConverterConfigurater();
	}
	
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

	
//	@Bean
//	@ConditionalOnProperty(name = JmsProperties.CONVERTER_KEY, havingValue = "jackson2", matchIfMissing = false)
//	public ObjectFactory<MappingJackson2MessageConverter> jackson2MessageConverter() {
////		return new MappingJackson2MessageConverter();
//		MappingJackson2MessageConverter json = new MappingJackson2MessageConverter();
//		return new ObjectProvider<MappingJackson2MessageConverter>() {
//
//			@Override
//			public MappingJackson2MessageConverter getObject() throws BeansException {
//				return json;
//			}
//
//			@Override
//			public MappingJackson2MessageConverter getObject(Object... args) throws BeansException {
//				return json;
//			}
//
//			@Override
//			public MappingJackson2MessageConverter getIfAvailable() throws BeansException {
//				return json;
//			}
//
//			@Override
//			public MappingJackson2MessageConverter getIfUnique() throws BeansException {
//				return json;
//			}
//
//		};
//	}
	

}
