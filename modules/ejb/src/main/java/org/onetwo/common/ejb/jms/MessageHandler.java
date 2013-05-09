package org.onetwo.common.ejb.jms;

import javax.jms.JMSException;
import javax.jms.Message;

public interface MessageHandler {
	
	public void doMessage(MessageProcessor processor, MessageSessionDelegate session, Message message) throws JMSException ;
	
//	public void afterMessage(MessageProcessor processor);

}
