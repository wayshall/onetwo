package org.onetwo.ext.rmqwithonsclient.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.consumer.TestConsumer;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSConsumerTest.RmqConsumerTestContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=RmqConsumerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RmqONSConsumerTest {

	@Test
	public void testReceiveMessage(){
		LangUtils.CONSOLE.exitIf("exit");
	}
	
	@EnableONSClient
	@Configuration
	@PropertySource("classpath:rmqwithonsclient-test.properties")
	@ComponentScan
	public static class RmqConsumerTestContext {
		@Bean
		public TestConsumer testConsumer(){
			return new TestConsumer();
		}
	}
}
