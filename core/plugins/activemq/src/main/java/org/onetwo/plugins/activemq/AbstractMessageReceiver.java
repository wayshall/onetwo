package org.onetwo.plugins.activemq;

import org.springframework.jms.support.converter.MessageConverter;

abstract public class AbstractMessageReceiver<T> implements MessageReceiver<T>{

	public Object handleMessage(T message){
		receive(message);
		return null;
	}
	
	abstract protected void receive(T message);
	
	public MessageConverter getMessageConverter(){
		return null;
	}
}
