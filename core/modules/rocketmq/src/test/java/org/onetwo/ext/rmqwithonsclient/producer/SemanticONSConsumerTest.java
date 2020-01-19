package org.onetwo.ext.rmqwithonsclient.producer;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.annotation.ONSConsumer;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.annotation.ONSSubscribe.IdempotentType;
import org.onetwo.ext.ons.consumer.ConsumerMeta;
import org.onetwo.ext.ons.consumer.ONSSubscribeProcessor;
import org.onetwo.ext.ons.producer.ONSProducerTest;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSConsumerTest.RmqConsumerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
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
		ConsumerMeta meta = consumers.get("SemanticConsumer1");
		assertThat(meta).isNotNull();
		assertThat(meta.getSubExpression()).isEqualTo("JuestTestTag");
		assertThat(meta.getIdempotentType()).isEqualTo(IdempotentType.DATABASE);
		
		meta = consumers.get("SemanticConsumer2");
		assertThat(meta).isNotNull();
		assertThat(meta.getSubExpression()).isEqualTo("JuestTestTag");
		assertThat(meta.getIdempotentType()).isEqualTo(IdempotentType.NONE);
	}
	
	@ONSConsumer
	public static class SemanticConsumer {
		@SemanicConsumerAnno(consumerId="SemanticConsumer1")
		public void test(ConsumContext consumContext) {
		}
		@SemanicConsumerAnno(consumerId="SemanticConsumer2", idempotent=IdempotentType.NONE)
		public void test2(ConsumContext consumContext) {
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@ONSSubscribe( 
				topic=ONSProducerTest.TOPIC, 
				tags="JuestTestTag")
	public static @interface SemanicConsumerAnno {
		@AliasFor(annotation=ONSSubscribe.class)
		String consumerId();
		
		@AliasFor(annotation=ONSSubscribe.class)
		IdempotentType idempotent() default IdempotentType.DATABASE;
	}
}
