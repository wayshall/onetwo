package org.onetwo.boot.module.activemq.classic;

import org.onetwo.boot.module.activemq.ActivemqProperties;
import org.onetwo.boot.module.jms.JmsDestinationConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value=ActivemqProperties.TYPE_KEY, havingValue = "classic")
public class ActiveMQClassicConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public JmsDestinationConverter destinationConverter(){
		return new ClassicDestinationConverter();
	}
	
}
