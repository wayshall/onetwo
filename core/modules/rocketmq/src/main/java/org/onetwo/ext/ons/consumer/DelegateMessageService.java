package org.onetwo.ext.ons.consumer;

import java.util.List;

import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.ons.ONSConsumerListenerComposite;
import org.onetwo.ext.ons.ONSUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DelegateMessageService implements InitializingBean {
	final Logger logger = ONSUtils.getONSLogger();
	final private MessageDeserializer messageDeserializer;
	private ONSConsumerListenerComposite consumerListenerComposite;
	
	
	public DelegateMessageService(MessageDeserializer messageDeserializer, ONSConsumerListenerComposite consumerListenerComposite) {
		super();
		this.messageDeserializer = messageDeserializer;
		this.consumerListenerComposite = consumerListenerComposite;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(messageDeserializer);
		Assert.notNull(consumerListenerComposite);
	}

	/***
	 * 返回最近一次消费上下文
	 * @author wayshall
	 * @param meta
	 * @param msgs
	 * @param context
	 * @return
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public ConsumContext processMessages(ConsumerMeta meta, List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		final CustomONSConsumer consumer = (CustomONSConsumer) meta.getListener();

		ConsumContext currentConetxt = null;
		for(MessageExt message : msgs){
			String msgId = ONSUtils.getMessageId(message);
			logger.info("received id: {}, topic: {}, tag: {}", msgId,  message.getTopic(), message.getTags());
			
//			Object body = consumer.deserialize(message);
			Object body = messageDeserializer.deserialize(message.getBody(), message);
			currentConetxt = ConsumContext.builder()
											.messageId(msgId)
											.message(message)
											.deserializedBody(body)
											.build();
			
			consumerListenerComposite.beforeConsumeMessage(currentConetxt);
			consumer.doConsume(currentConetxt);
			consumerListenerComposite.afterConsumeMessage(currentConetxt);
			logger.info("consumed message. id: {}, topic: {}, tag: {}, body: {}", msgId,  message.getTopic(), message.getTags(), body);
		}
		return currentConetxt;
	}

}
