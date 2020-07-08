package org.onetwo.boot.module.activemq.mqtt;

import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.InBoundClientProps;
import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.OutBoundClientProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@ConditionalOnProperty(value = ActiveMQTTProperties.SERVER_URLS_KEY)
public class ActiveMQTTConfiguration {
	
	@Autowired
	private ActiveMQTTProperties activeMQTTProperties;
	
	@Bean
	public MqttPahoClientFactory mqttPahoClientFactory() {
		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
		clientFactory.setServerURIs(activeMQTTProperties.getServerUrls());
		clientFactory.setUserName(activeMQTTProperties.getUsername());
		clientFactory.setPassword(activeMQTTProperties.getPassword());
		clientFactory.setCleanSession(activeMQTTProperties.isCleanSession());
		clientFactory.setConnectionTimeout(activeMQTTProperties.getConnectionTimeout());
		clientFactory.setKeepAliveInterval(activeMQTTProperties.getKeepAliveInterval());
		clientFactory.setSslProperties(activeMQTTProperties.getSsl());
		clientFactory.setConsumerStopAction(activeMQTTProperties.getConsumerStopAction());
		return clientFactory;
	}
	
	@Configuration
	@ConditionalOnProperty(value = OutBoundClientProps.CLIENT_ID_KEY)
	protected static class OutboudrConfiguration {
		@Autowired
		private ActiveMQTTProperties activeMQTTProperties;
//		@Value("${spring.application.name:''}")
//		private String defaultClientId;
		
		@Bean(name = Mqtts.OUTBOUND_CHANNEL)
		public MessageChannel mqttOutboundChannel() {
			return new DirectChannel();
		}
		
		@Bean
		@ServiceActivator(inputChannel = Mqtts.OUTBOUND_CHANNEL)
		public MqttPahoMessageHandler mqttOutbound(MqttPahoClientFactory clientFactory) {
			OutBoundClientProps client = activeMQTTProperties.getOutbound();
			String clientId = client.getClientId();
			Assert.hasText(clientId, "Outbound clientId can not be blank!");
			
			MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, clientFactory);
			handler.setAsync(client.isAsync());
			handler.setAsyncEvents(client.isAsyncEvents());
			handler.setDefaultQos(client.getDefaultQos());
			handler.setDefaultRetained(client.isDefaultRetained());
			handler.setCompletionTimeout(client.getCompletionTimeout());
			
			return handler;
		}
	}
	
	/****
	 * 如果需要配置不同的消费者，复制参考此配置
	 * 
	 * @author way
	 *
	 */
	@Configuration
	@ConditionalOnProperty(value = InBoundClientProps.CLIENT_ID_KEY)
	protected static class InboundConfiguration {
		@Autowired
		private ActiveMQTTProperties activeMQTTProperties;
//		@Value("${spring.application.name:''}")
//		private String defaultClientId;
		
		@Bean(name = Mqtts.INBOUND_CHANNEL)
	    public MessageChannel mqttInputChannel() {
	        return new DirectChannel();
	    }
		
		@Bean
		public MessageProducerSupport mqttInbound(MqttPahoClientFactory clientFactory) {
			InBoundClientProps client = activeMQTTProperties.getInbound();

			Assert.notEmpty(client.getTopics(), "Inbound topics can not be empty!");
			
			String clientId = client.getClientId();
			Assert.hasText(clientId, "Inbound clientId can not be blank!");
			
			MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, clientFactory, client.getTopics());
			adapter.setQos(client.getQos());
			adapter.setCompletionTimeout(client.getCompletionTimeout());
			adapter.setConverter(new DefaultPahoMessageConverter());
			
			return adapter;
		}
		
//		@Bean
//		@ServiceActivator(inputChannel = Mqtts.INBOUND_CHANNEL) // 绑定消费者
//		public MessageHandler handler() {
//			return new ReceiveMessageHandler();
//		}
	}

}
