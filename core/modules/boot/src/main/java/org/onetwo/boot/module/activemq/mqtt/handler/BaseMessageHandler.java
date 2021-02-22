package org.onetwo.boot.module.activemq.mqtt.handler;

import java.io.Serializable;

import org.onetwo.boot.module.activemq.mqtt.Mqtts;
import org.onetwo.boot.module.activemq.mqtt.data.ConvertedMessage;
import org.onetwo.boot.module.activemq.mqtt.data.LinkedMapMessage;
import org.onetwo.boot.module.activemq.mqtt.event.ConvertedMessageEvent;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * @author weishao zeng
 * <br/>
 */
abstract public class BaseMessageHandler<T extends Serializable> implements MessageHandler, ApplicationContextAware {

	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());
	protected Class<T> bodyClass;
	protected ApplicationContext applicationContext;

	public BaseMessageHandler() {
		this(null);
	}
	
	@SuppressWarnings("unchecked")
	public BaseMessageHandler(Class<T> bodyClass) {
		if(bodyClass==null){
			this.bodyClass = (Class<T>)ReflectUtils.getSuperClassGenricType(this.getClass(), BaseMessageHandler.class);
		}else{
			this.bodyClass = bodyClass;
		}
	}

	protected ConvertedMessage<T> convertedMessage(Message<?> message) throws MessagingException {
		ConvertedMessage<T> msg = Mqtts.convertedMessage(message, bodyClass);
		logger.info("已转换消息, topic: {}, body: {}", msg.getTopic(), msg.getBody());
		return msg;
	}

	protected LinkedMapMessage linkedMapMessage(Message<?> message) throws MessagingException {
		LinkedMapMessage msg = Mqtts.linkedMapMessage(message);
		logger.info("已转换消息, topic: {}, body: {}", msg.getTopic(), msg.getBody());
		return msg;
	}
	
	/****
	 * 发布为spring容器事件
	 * @author weishao zeng
	 * @param message
	 */
	protected void publishAsContainerEvent(ConvertedMessage<T> message) {
		ConvertedMessageEvent<T> event = new ConvertedMessageEvent<>(this);
		event.setMessage(message);
		this.applicationContext.publishEvent(event);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
