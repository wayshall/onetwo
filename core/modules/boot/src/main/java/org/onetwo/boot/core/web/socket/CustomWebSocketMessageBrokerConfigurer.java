package org.onetwo.boot.core.web.socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.onetwo.boot.core.web.socket.WebSocketProperties.BrokerProps;
import org.onetwo.boot.core.web.socket.WebSocketProperties.StompProps;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

public class CustomWebSocketMessageBrokerConfigurer extends AbstractWebSocketMessageBrokerConfigurer {
	
	@Autowired
	WebSocketProperties properties;
	@Autowired(required = false)
	PrincipalHandshakeHandler principalHandshakeHandler;
	@Autowired
	List<WebSocketHandlerDecoratorFactory> factories;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		BrokerProps broker = properties.getBroker();

		List<String> simpleBrokers = new ArrayList<>();
		if (StringUtils.isNotBlank(broker.getUserPrefix())) {
			// 使用SimpMessageSendingOperations.convertAndSendToUser(user, destination)发送消息时，
			// 实际会发送到地址：/userDestinationPrefix/userName/destination
			registry.setUserDestinationPrefix(broker.getUserPrefix());
			simpleBrokers.add(broker.getUserPrefix());
		}
		
		if (!LangUtils.isEmpty(broker.getSimplePrefixes())) {
			simpleBrokers.addAll(Arrays.asList(broker.getSimplePrefixes()));
		}
		if (!simpleBrokers.isEmpty()) {
			registry.enableSimpleBroker(simpleBrokers.toArray(new String[0]));
		}
		if (!LangUtils.isEmpty(broker.getAppPrefixes())) {
			registry.setApplicationDestinationPrefixes(broker.getAppPrefixes());
		}
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		StompProps stomp = properties.getStomp();
		
		if (!LangUtils.isEmpty(stomp.getEndpoints())) {
			StompWebSocketEndpointRegistration reg = registry.addEndpoint(stomp.getEndpoints());
			if (!LangUtils.isEmpty(stomp.getAllowedOrigins())) {
				reg.setAllowedOrigins(stomp.getAllowedOrigins());
			}
			if (principalHandshakeHandler!=null) {
				reg.setHandshakeHandler(principalHandshakeHandler);
			}
			reg.withSockJS();
			
		}
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		if (factories!=null) {
			registration.setDecoratorFactories(factories.toArray(new WebSocketHandlerDecoratorFactory[0]));
		}
	}

}
