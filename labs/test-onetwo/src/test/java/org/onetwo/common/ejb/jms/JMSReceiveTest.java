package org.onetwo.common.ejb.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.utils.propconf.PropConfig;

public class JMSReceiveTest {
	
	private PropConfig config;
	
	@Before
	public void setup(){
		config = AppConfig.create("jndi-jms.properties");
	}
	
	@Test
	public void testJMS(){
	}
	
	public static void main(String[] args) throws JMSException{
		PropConfig config = AppConfig.create("jndi-jms.properties");
		MessageProcessor processor = MessageFactory.createSender(config);
		/*processor.setMessageHandler(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				System.out.println(DateUtil.nowString()+":"+message);
			}
		});*/
		processor.onMessageHandler(true, new MessageHandler() {
			
			@Override
			public void doMessage(MessageProcessor processor, MessageSessionDelegate session, Message msg) throws JMSException {
				System.out.println("msg.id: " + msg.getJMSMessageID());
				System.out.println("msg: " + ((TextMessage)msg).getText());
				msg.acknowledge();
			}
		});
	}

}
