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
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
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

	@SuppressWarnings("rawtypes")
	public ConsumeConcurrentlyStatus processMessages(ConsumerMeta meta, List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		final CustomONSConsumer consumer = (CustomONSConsumer) meta.getListener();

		ConsumContext currentConetxt = null;
		try {
			if(meta.getIgnoreOffSetThreshold()>0){
				long diff = ONSUtils.getMessageDiff(msgs.get(0));
				if(diff>meta.getIgnoreOffSetThreshold()){
					logger.info("message offset diff[{}] is greater than ignoreOffSetThreshold[{}], ignore!", diff, meta.getIgnoreOffSetThreshold());
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			}
			for(MessageExt message : msgs){
				String msgId = ONSUtils.getMessageId(message);
				logger.info("received id: {}, topic: {}, tag: {}", msgId,  message.getTopic(), message.getTags());
				
//				Object body = consumer.deserialize(message);
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
		} catch (Exception e) {
			String errorMsg = "consume message error.";
			if(currentConetxt!=null){
				consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
				errorMsg += "currentMessage id: "+currentConetxt.getMessageId()+", topic: "+currentConetxt.getMessage().getTopic()+
								", tag: "+currentConetxt.getMessage().getTags()+", body: " + currentConetxt.getDeserializedBody();
			}
			logger.error(errorMsg, e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//			throw new BaseException(e);
		}
//		logger.info("consumed firstMessage. id: {}, topic: {}, tag: {}", firstMessage.getMsgId(),  firstMessage.getTopic(), firstMessage.getTags());

		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
