package org.onetwo.ext.ons;

import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.alimq.MessageSerializer;
import org.onetwo.ext.ons.ONSProperties.SendMode;
import org.onetwo.ext.ons.consumer.ONSPushConsumerStarter;
import org.onetwo.ext.ons.transaction.AsyncDatabaseTransactionMessageInterceptor;
import org.onetwo.ext.ons.transaction.CompensationSendMessageTask;
import org.onetwo.ext.ons.transaction.DatabaseTransactionMessageInterceptor;
import org.onetwo.ext.ons.transaction.DbmSendMessageRepository;
import org.onetwo.ext.ons.transaction.DefaultDatabaseTransactionMessageInterceptor;
import org.onetwo.ext.ons.transaction.MessageBodyStoreSerializer;
import org.onetwo.ext.ons.transaction.SendMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

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
		starter.setConsumerListenerComposite(onsConsumerListenerComposite());
		return starter;
	}
	
	@Bean
	public ONSConsumerListenerComposite onsConsumerListenerComposite(){
		return new ONSConsumerListenerComposite();
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
	@EnableScheduling
	protected static class TransactionalConfiguration {
		@Autowired
		private ONSProperties onsProperties;

		@Bean
		@ConditionalOnMissingBean(DefaultDatabaseTransactionMessageInterceptor.class)
		public DatabaseTransactionMessageInterceptor databaseTransactionMessageInterceptor(SendMessageRepository sendMessageRepository){
			DefaultDatabaseTransactionMessageInterceptor interceptor = null;
			SendMode sendMode = onsProperties.getTransactional().getSendMode();
			if(sendMode==SendMode.ASYNC){
				interceptor = new AsyncDatabaseTransactionMessageInterceptor();
			}else{
				interceptor = new DefaultDatabaseTransactionMessageInterceptor();
			}
			interceptor.setSendMessageRepository(sendMessageRepository);
			return interceptor;
		}
		
		@Bean
		@ConditionalOnMissingBean(SendMessageRepository.class)
		public SendMessageRepository sendMessageRepository(){
			return new DbmSendMessageRepository();
		}
		
		@Bean
		@ConditionalOnMissingBean(MessageBodyStoreSerializer.class)
		public MessageBodyStoreSerializer messageBodyStoreSerializer(){
			return MessageBodyStoreSerializer.INSTANCE;
		}
		
		@Bean
		@ConditionalOnMissingBean(CompensationSendMessageTask.class)
		public CompensationSendMessageTask compensationSendMessageTask(){
			return new CompensationSendMessageTask();
		}
	}
	
}
