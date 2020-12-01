package org.onetwo.boot.core.web.socket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableConfigurationProperties(WebSocketProperties.class)
@ConditionalOnProperty(value = WebSocketProperties.ENABLED_KEY, matchIfMissing = false)
@ConditionalOnClass(WebSocketMessageBrokerConfigurer.class)
@EnableWebSocketMessageBroker
public class WebsocketConfiguration {

	@Bean
	public CustomWebSocketMessageBrokerConfigurer customWebSocketMessageBrokerConfigurer() {
		return new CustomWebSocketMessageBrokerConfigurer();
	}
	
	@Bean
	public PrincipalHandshakeHandler principalHandshakeHandler() {
		return new PrincipalHandshakeHandler();
	}
	
	@Bean
	public StompMessageSender stompMessageSender() {
		return new StompMessageSender();
	}
}
