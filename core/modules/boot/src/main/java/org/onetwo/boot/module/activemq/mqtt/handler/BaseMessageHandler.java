package org.onetwo.boot.module.activemq.mqtt.handler;

import org.onetwo.boot.module.activemq.mqtt.Mqtts;
import org.onetwo.boot.module.activemq.mqtt.data.ConvertedMessage;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * @author weishao zeng
 * <br/>
 */
abstract public class BaseMessageHandler<T> implements MessageHandler {

	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());
	protected Class<T> bodyClass;

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

}
