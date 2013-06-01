package org.onetwo.common.ejb.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.Consoler.ConsoleAction;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.onetwo.common.utils.propconf.PropConfig;

public class JMSListenerTest {
	
	private PropConfig config;
	
	@Before
	public void setup(){
		config = AppConfig.create("jndi-jms.properties");
	}
	
	@Test
	public void testJMS(){
	}
	
	public static void main(String[] args) throws JMSException, Exception{
		PropConfig config = AppConfig.create("jndi-jms.properties");
		final MessageProcessor processor = MessageFactory.createSender(config);
		processor.onMessageHandler(false, new MessageHandler() {
			
			
			@Override
			public void doMessage(MessageProcessor processor, MessageSessionDelegate session, Message message) throws JMSException {
				MapMessage mapMsg = (MapMessage) message;
				LangUtils.println("msg:${0}, ${1}", mapMsg.getString("code"), mapMsg.getString("status"));
				message.acknowledge();
			}
		});
		LangUtils.CONSOLE.waitIf("exit", new ConsoleAction(){

			@Override
			public void execute(String in) {
				processor.destroy();
				System.exit(0);
			}
			
		});
	}

}
