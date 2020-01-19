package org.onetwo.ext.rmqwithonsclient.producer;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.annotation.ONSConsumer;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.consumer.ConsumerMeta;
import org.onetwo.ext.ons.consumer.ONSSubscribeProcessor;
import org.onetwo.ext.ons.producer.ONSProducerTest;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSConsumerTest.RmqConsumerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=RmqConsumerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SemanticONSConsumerTest {
	
	@Autowired
	ONSSubscribeProcessor subscribeProcessor;

	@Test
	public void testSemanticConsumer(){
		Map<String, ConsumerMeta> consumers = Maps.newHashMap();
		subscribeProcessor.parse(consumers);
		ConsumerMeta meta = consumers.get("SemanticConsumer");
	}
	
	@ONSConsumer
	public static class SemanticConsumer {
		
		@ONSSubscribe(consumerId="SemanticConsumer", 
				topic=ONSProducerTest.TOPIC, 
				tags=ONSProducerTest.ORDER_PAY)
		public void test(ConsumContext consumContext) {
		}
	}
	
}
