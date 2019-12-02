package org.onetwo.boot.mq;

import org.onetwo.boot.mq.interceptor.DatabaseTransactionMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SimpleDatabaseTransactionMessageInterceptor;
import org.onetwo.boot.mq.repository.DbmSendMessageRepository;
import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.boot.mq.serializer.MessageBodyStoreSerializer;
import org.onetwo.boot.mq.task.DeleteSentMessageTask;
import org.onetwo.boot.mq.task.SendMessageTask;
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
@ConditionalOnProperty(value=MQProperties.TRANSACTIONAL_ENABLED_KEY, matchIfMissing=true)
@EnableConfigurationProperties(MQProperties.class)
public class MQTransactionalConfiguration {

	@Autowired
	private MQProperties mqProperties;

	@Bean
	@ConditionalOnMissingBean(DatabaseTransactionMessageInterceptor.class)
	public DatabaseTransactionMessageInterceptor databaseTransactionMessageInterceptor(SendMessageRepository sendMessageRepository){
		SimpleDatabaseTransactionMessageInterceptor interceptor = new SimpleDatabaseTransactionMessageInterceptor();
		/*SendMode sendMode = mqProperties.getTransactional().getSendMode();
		if(sendMode==SendMode.ASYNC){
			interceptor.setUseAsync(true);
		}else{
			interceptor.setUseAsync(false);
		}*/
		interceptor.setTransactionalProps(mqProperties.getTransactional());
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
		return MessageBodyStoreSerializer.DEFAULT;
	}
	
	/***
	 * 默认通过配置控制，可扩展MQTaskConfiguration通过代码控制
	 * @author way
	 *
	 */
	@Configuration
	@EnableScheduling
	static class InnerTaskConfiguration extends MQTaskConfiguration {

		public InnerTaskConfiguration(MQProperties mqProperties) {
			super(mqProperties);
		}
		
		@Bean
		@ConditionalOnProperty(value=MQProperties.TRANSACTIONAL_SEND_TASK_ENABLED_KEY, matchIfMissing=false)
		@ConditionalOnMissingBean(SendMessageTask.class)
		public SendMessageTask compensationSendMessageTask(){
			return super.createSendMessageTask();
		}
		
		@Bean
		@ConditionalOnProperty(value=MQProperties.TRANSACTIONAL_DELETE_TASK_ENABLED_KEY, matchIfMissing=false)
		@ConditionalOnMissingBean(DeleteSentMessageTask.class)
		public DeleteSentMessageTask deleteSentMessageTask(){
			return super.createDeleteSentMessageTask();
		}
	}
	
}
