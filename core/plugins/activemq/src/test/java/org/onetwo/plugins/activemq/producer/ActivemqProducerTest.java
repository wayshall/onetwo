package org.onetwo.plugins.activemq.producer;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(loader=ActivemqProducerLoader.class )
public class ActivemqProducerTest extends AbstractJUnit4SpringContextTests {
	
	@Resource
	private ProducerServiceImpl producerServiceImpl;
	
	@Test
	public void testSendMessage(){
		System.out.println("sending");
		this.producerServiceImpl.sendMessage("myqueue", "mytest message");
		System.out.println("send");
	}

}
