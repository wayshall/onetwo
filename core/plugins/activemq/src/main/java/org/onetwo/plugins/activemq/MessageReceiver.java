package org.onetwo.plugins.activemq;

import org.springframework.jms.support.converter.MessageConverter;

public interface MessageReceiver<T> {
	
	Object handleMessage(T message);
	
	String getDestinationName();
	
	MessageConverter getMessageConverter();

}
