package org.onetwo.plugins.activemq.consumer;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(loader=ActivemqConsumerLoader.class )
public class ActivemqConsumerTest extends AbstractJUnit4SpringContextTests {
	
	@Test
	public void testSendMessage(){
		System.out.println("ActivemqConsumerTest");
		LangUtils.CONSOLE.exitIf("exit");
	}

}
