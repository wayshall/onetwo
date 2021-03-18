package org.onetwo.boot.module.activemq.mqtt.event;

import java.io.Serializable;

import org.onetwo.boot.module.activemq.mqtt.data.ConvertedMessage;
import org.springframework.context.ApplicationEvent;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class ConvertedMessageEvent<T extends Serializable> extends ApplicationEvent {

	private ConvertedMessage<T> message;
	
	public ConvertedMessageEvent(Object source) {
		super(source);
	}

	public ConvertedMessage<T> getMessage() {
		return message;
	}

	public void setMessage(ConvertedMessage<T> message) {
		this.message = message;
	}
	
}
