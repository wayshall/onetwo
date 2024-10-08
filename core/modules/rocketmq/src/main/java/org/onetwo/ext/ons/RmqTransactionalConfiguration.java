package org.onetwo.ext.ons;

import org.apache.rocketmq.client.producer.TransactionListener;
import org.onetwo.boot.mq.MQProperties;
import org.onetwo.boot.mq.cosume.DbmReceiveMessageRepository;
import org.onetwo.boot.mq.cosume.ReceiveMessageRepository;
import org.onetwo.boot.mq.interceptor.DatabaseTransactionMessageInterceptor;
import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.ext.ons.consumer.StoreConsumerListener;
import org.onetwo.ext.ons.producer.OnsDatabaseTransactionMessageInterceptor;
import org.onetwo.ext.rocketmq.transaction.GenericRmqTransactionListener;
import org.onetwo.ext.rocketmq.transaction.RmqTransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value=MQProperties.TRANSACTIONAL_ENABLED_KEY, matchIfMissing=true)
@EnableConfigurationProperties({MQProperties.class, ONSProperties.class})
@ConditionalOnClass(name="org.onetwo.common.db.spi.BaseEntityManager")
public class RmqTransactionalConfiguration {
	@Autowired
	private MQProperties mqProperties;


	@Bean
//	@ConditionalOnProperty(value=MQProperties.TRANSACTIONAL_ENABLED_KEY, matchIfMissing=true)
	public DatabaseTransactionMessageInterceptor databaseTransactionMessageInterceptor(SendMessageRepository sendMessageRepository){
		OnsDatabaseTransactionMessageInterceptor interceptor = new OnsDatabaseTransactionMessageInterceptor();
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
//	@ConditionalOnBean(BaseEntityManager.class)
	public StoreConsumerListener storeConsumerListener() {
		StoreConsumerListener listener = new StoreConsumerListener();
		return listener;
	}
	
	@Bean
	@ConditionalOnMissingBean(ReceiveMessageRepository.class)
//	@ConditionalOnBean(BaseEntityManager.class)
	public ReceiveMessageRepository receiveMessageRepository() {
		return new DbmReceiveMessageRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(TransactionListener.class)
	public TransactionListener rmqTransactionListener() {
		GenericRmqTransactionListener listener = new GenericRmqTransactionListener();
		return listener;
	}
	
	@Bean
	public RmqTransactionLogService rmqTransactionLogService() {
		return new RmqTransactionLogService();
	}
}
