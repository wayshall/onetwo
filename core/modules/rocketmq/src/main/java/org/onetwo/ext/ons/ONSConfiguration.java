package org.onetwo.ext.ons;

import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.alimq.MessageSerializer;
import org.onetwo.ext.ons.consumer.ONSPushConsumerStarter;
import org.onetwo.ext.ons.transaction.DatabaseTransactionListener;
import org.onetwo.ext.ons.transaction.DbmSendMessageRepository;
import org.onetwo.ext.ons.transaction.SendMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(ONSProperties.class)
public class ONSConfiguration {
	@Autowired
	private ONSProperties onsProperties;
	
	@Bean
	public ONSPushConsumerStarter onsPushConsumerStarter(MessageDeserializer messageDeserializer){
		ONSPushConsumerStarter starter = new ONSPushConsumerStarter(messageDeserializer);
		starter.setOnsProperties(onsProperties);
		return starter;
	}
	
	@Bean
	@ConditionalOnMissingBean(MessageDeserializer.class)
	public MessageDeserializer onsMessageDeserializer(){
		return onsProperties.getSerializer().getDeserializer();
	}
	
	@Bean
	@ConditionalOnMissingBean(MessageSerializer.class)
	public MessageSerializer onsMessageSerializer(){
		return onsProperties.getSerializer().getSerializer();
	}
	
	
	@Bean
	@ConditionalOnMissingBean(SendMessageLogInterceptor.class)
	public SendMessageLogInterceptor sendMessageLogInterceptor(){
		return new SendMessageLogInterceptor();
	}

	@Configuration
	@ConditionalOnProperty(ONSProperties.TRANSACTIONAL_ENABLED_KEY)
	protected static class TransactionalConfiguration {

		@Bean
		@ConditionalOnMissingBean(DatabaseTransactionListener.class)
		public DatabaseTransactionListener satabaseTransactionListener(){
			return new DatabaseTransactionListener();
		}
		@Bean
		@ConditionalOnMissingBean(SendMessageRepository.class)
		public SendMessageRepository sendMessageRepository(){
			return new DbmSendMessageRepository();
		}
	}
	
}
