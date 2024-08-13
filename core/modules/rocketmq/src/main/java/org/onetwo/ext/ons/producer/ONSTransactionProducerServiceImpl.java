package org.onetwo.ext.ons.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.alimq.ExtMessage;
import org.onetwo.ext.alimq.OnsMessage;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.rocketmq.transaction.DefaultMQProducerImplExt;
import org.onetwo.ext.rocketmq.transaction.RmqTransactionContext;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * https://rocketmq.apache.org/zh/docs/4.x/producer/06message5
 * ons普通producer和事务produer不能混用
 * @author wayshall
 * <br/>
 */
public class ONSTransactionProducerServiceImpl extends ONSProducerServiceImpl implements InitializingBean, DisposableBean, DefaultProducerService, TransactionProducerService {
	
	private TransactionListener transactionListener;
	
	protected TransactionMQProducer newMQProducer(String groupName) {
//		GenericRmqTransactionListener transactionListener = SpringUtils.getBean(applicationContext, GenericRmqTransactionListener.class);
		TransactionMQProducer producer = new TransactionMQProducer(groupName);
		DefaultMQProducerImplExt producerExt = new DefaultMQProducerImplExt(producer);
		producer.setTransactionListener(transactionListener);
		ConfigurablePropertyAccessor bw = SpringUtils.newPropertyAccessor(producer, true);
		// 替换掉默认的defaultMQProducerImpl
		bw.setPropertyValue("defaultMQProducerImpl", producerExt);
		return producer;
	}

	
	@Override
	public boolean isTransactional() {
		return true;
	}

	public TransactionMQProducer getProducer() {
		return getRawProducer(TransactionMQProducer.class);
	}

	@Override
	public void sendMessageInTransaction(String topic, String tags, Object body){
		SimpleMessage message = SimpleMessage.builder()
											.topic(topic)
											.tags(tags)
											.body(body)
											.build();
		SendResult result = this.sendMessageInTransaction(message, null);
		checkSendResult(result);
	}
	

	public SendResult sendMessageInTransaction(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate){
		Object body = onsMessage.getBody();
		if(body instanceof ExtMessage){
			return sendRawMessageInTransaction((ExtMessage)body, interceptorPredicate);
		}
		
		ExtMessage message = onsMessage.toMessage();
		configMessage(message, onsMessage);
		
		return sendRawMessageInTransaction(message, interceptorPredicate);
	}
	
	protected SendResult doSendRawMessageInTransaction(ExtMessage message){
		TransactionSendResult result = null;
		try {
			if (logger.isInfoEnabled()) {
				logger.info("do send raw transactional message. keys: {}", message.getKey());
			}
//			return this.send(message);
			RmqTransactionContext ctx = new RmqTransactionContext();
			result = this.getProducer().sendMessageInTransaction(message, ctx);

			if (logger.isInfoEnabled()) {
				logger.info("transactional message has been sent. key: {}, result: {}", message.getKey(), result);
			}
			checkSendResult(result);
		} catch (MQClientException e) {
			handleException(e, message);
		}catch (Throwable e) {
			handleException(e, message);
		}
		return result;
	}

	public SendResult sendRawMessageInTransaction(ExtMessage message, final InterceptorPredicate interPredicate) {
		return this.sendRawMessage(message, null, () -> this.doSendRawMessageInTransaction(message), ctx -> {
			ctx.setTransactional(true);
		});
	}
	
	@Override
	public void sendMessageInTransaction(ExtMessage message, RmqTransactionContext tranactionListenerArg) {
		this.sendRawMessage(message, null, () -> this.doSendRawMessageInTransaction(message), ctx -> {
			ctx.setTransactional(true);
		});
//		TransactionSendResult result;
//		try {
//			result = this.getProducer().sendMessageInTransaction(message, tranactionListenerArg);
//			this.checkSendResult(result);
//		} catch (MQClientException e) {
////			throw new MQException("send rocketmq transaction message error: " + e.getMessage(), e);
//			handleException(e, message);
//		}
	}

	@Autowired
	public void setTransactionListener(TransactionListener transactionListener) {
		this.transactionListener = transactionListener;
	}
	
	
	
}
