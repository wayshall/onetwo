package org.onetwo.plugins.activemq.consumer;

import org.onetwo.plugins.activemq.AbstractMessageReceiver;
import org.onetwo.plugins.activemq.MailInfo;

public class ConsumerServiceImpl extends AbstractMessageReceiver<MailInfo> {

	@Override
	public void receive(MailInfo message) {
		System.out.println("recive: " + message.getSubject());
		
	}

	@Override
	public String getDestinationName() {
		return "myqueue";
	}

}
