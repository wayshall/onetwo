package org.onetwo.boot.module.activemq.mqtt;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * 当channel包含了多个topic时，继承此基类可以通过topic过滤和转换payload
 * 但需要channel的类型PublishSubscribeChannel（广播给每个订阅者），
 * 因为如果是DirectChannel，同一个channel实际上只会分配给一个订阅者
 * @Bean(name = "channelName")
	public MessageChannel channelName() {
		return new PublishSubscribeChannel();
	}
 * @author weishao zeng
 * <br/>
 */
abstract public class BaseMultiTopicMessageHandler<T> implements MessageHandler {
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	private String topic;
	private Class<T> payloadClass;
	
	public BaseMultiTopicMessageHandler(String topic, Class<T> payloadClass) {
		super();
		this.topic = topic;
		this.payloadClass = payloadClass;
	}

	protected String resolveTopic(Message<?> message) {
		String topic = Mqtts.getTopic(message);
		return topic;
	}
	
	protected T convertPayload(Message<?> message, Class<T> type) {
		T payload = Mqtts.convertPayload(message, type);
		return payload;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		String topic = resolveTopic(message);
		if (logger.isInfoEnabled()) {
			logger.info("收到mqtt消息: {}", message);
		}
		if (topic.equals(this.topic)) {
			T payload = convertPayload(message, this.payloadClass);
			doHandleMessage(payload);
		} else {
			logger.info("topic不匹配，忽略消费. current consumer subscribe topic: {}, message topic: {}", this.topic, topic);
		}
	}
	
	abstract protected void doHandleMessage(T payload) throws MessagingException;

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setPayloadClass(Class<T> payloadClass) {
		this.payloadClass = payloadClass;
	}

}
