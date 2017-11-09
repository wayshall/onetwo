package org.onetwo.ext.ons.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.consumer.ONSConsumerTest.ConsumerTestContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=ConsumerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ONSConsumerTest {

	@Test
	public void testReceiveMessage(){
		LangUtils.CONSOLE.exitIf("exit");
	}
	
	@EnableONSClient
	@Configuration
	@PropertySource("classpath:ons.properties")
	public static class ConsumerTestContext {
		@Bean
		public TestConsumer testConsumer(){
			return new TestConsumer();
		}
	}
}
