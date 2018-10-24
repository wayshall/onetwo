package org.onetwo.boot.mq;

import org.onetwo.boot.mq.MQProperties.DeleteTaskProps;
import org.onetwo.boot.mq.MQProperties.SendMode;
import org.onetwo.boot.mq.MQProperties.TaskLocks;
import org.onetwo.boot.mq.interceptor.DatabaseTransactionMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SimpleDatabaseTransactionMessageInterceptor;
import org.onetwo.boot.mq.repository.DbmSendMessageRepository;
import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.boot.mq.serializer.MessageBodyStoreSerializer;
import org.onetwo.boot.mq.task.CompensationSendMessageTask;
import org.onetwo.boot.mq.task.DeleteSentMessageTask;
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
@ConditionalOnProperty(MQProperties.TRANSACTIONAL_ENABLED_KEY)
@EnableScheduling
@EnableConfigurationProperties(MQProperties.class)
public class MQTransactionalConfiguration {

	@Autowired
	private MQProperties mqProperties;

	@Bean
	@ConditionalOnMissingBean(DatabaseTransactionMessageInterceptor.class)
	public DatabaseTransactionMessageInterceptor databaseTransactionMessageInterceptor(SendMessageRepository sendMessageRepository){
		SimpleDatabaseTransactionMessageInterceptor interceptor = new SimpleDatabaseTransactionMessageInterceptor();
		SendMode sendMode = mqProperties.getTransactional().getSendMode();
		if(sendMode==SendMode.ASYNC){
			interceptor.setUseAsync(true);
		}else{
			interceptor.setUseAsync(false);
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
		return MessageBodyStoreSerializer.DEFAULT;
	}
	
	@Bean
	@ConditionalOnProperty(value=MQProperties.TRANSACTIONAL_SEND_TASK_ENABLED_KEY, matchIfMissing=false)
	public CompensationSendMessageTask compensationSendMessageTask(){
		CompensationSendMessageTask task = new CompensationSendMessageTask();
		TaskLocks taskLock = mqProperties.getTransactional().getSendTask().getLock();
		if(taskLock==TaskLocks.REDIS){
			task.setUseReidsLock(true);
		}
		return task;
	}
	
	@Bean
	@ConditionalOnProperty(value=MQProperties.TRANSACTIONAL_DELETE_TASK_ENABLED_KEY, matchIfMissing=false)
	public DeleteSentMessageTask deleteSentMessageTask(){
		DeleteSentMessageTask task = new DeleteSentMessageTask();
		DeleteTaskProps deleteProps = mqProperties.getTransactional().getDeleteTask();
		if(deleteProps.getLock()==TaskLocks.REDIS){
			task.setUseReidsLock(true);
		}
		task.setDeleteBeforeAt(deleteProps.getDeleteBeforeAt());
		return task;
	}
}
