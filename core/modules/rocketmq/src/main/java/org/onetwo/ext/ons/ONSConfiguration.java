package org.onetwo.ext.ons;

import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.alimq.MessageSerializer;
import org.onetwo.ext.ons.consumer.ONSPushConsumerStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(ONSProperties.class)
public class ONSConfiguration {
	@Autowired
	private ONSProperties onsProperties;
	
	@Bean
	public ONSPushConsumerStarter onsPushConsumerStarter(ONSConsumerListenerComposite composite, MessageDeserializer messageDeserializer){
		ONSPushConsumerStarter starter = new ONSPushConsumerStarter(messageDeserializer);
		starter.setOnsProperties(onsProperties);
		starter.setConsumerListenerComposite(composite);
		return starter;
	}
	
	@Bean
	@ConditionalOnMissingBean(MessageDeserializer.class)
	public MessageDeserializer onsMessageDeserializer(){
		return onsProperties.getSerializer().getDeserializer();
	}
	
	@Bean
	@ConditionalOnMissingBean(MessageSerializer.class)
	public MessageSerializer onsMessageSerializer(){
		return onsProperties.getSerializer().getSerializer();
	}
	
	@Bean
	@ConditionalOnMissingBean(ONSConsumerListenerComposite.class)
	public ONSConsumerListenerComposite onsConsumerListenerComposite(){
		ONSConsumerListenerComposite composite = new ONSConsumerListenerComposite();
		return composite;
	}
	
	@Bean
	@ConditionalOnMissingBean(ONSProducerListenerComposite.class)
	public ONSProducerListenerComposite onsProducerListenerComposite(){
		return new ONSProducerListenerComposite();
	}
	
}
