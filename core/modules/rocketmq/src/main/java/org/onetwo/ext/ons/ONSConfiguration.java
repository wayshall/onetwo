package org.onetwo.ext.ons;

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
	public ONSPushConsumerStarter onsPushConsumerStarter(ONSConsumerListenerComposite composite){
		ONSPushConsumerStarter starter = new ONSPushConsumerStarter();
		starter.setOnsProperties(onsProperties);
		starter.setConsumerListenerComposite(composite);
		return starter;
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
