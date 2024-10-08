package org.onetwo.boot.module.activemq.mqtt;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.InBoundClientProps;
import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.MessageConverters;
import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.OutBoundClientProps;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@ConditionalOnProperty(value = ActiveMQTTProperties.SERVER_URLS_KEY)
@EnableConfigurationProperties(ActiveMQTTProperties.class)
public class ActiveMQTTConfiguration {
	
	public static final String BEAN_MQTT_PAHO_CLIENT_FACTORY = "mqttPahoClientFactory";
	
	@Autowired
	private ActiveMQTTProperties activeMQTTProperties;
	
	@Bean(name = BEAN_MQTT_PAHO_CLIENT_FACTORY)
	public MqttPahoClientFactory mqttPahoClientFactory() {
		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
		
//		clientFactory.setServerURIs(activeMQTTProperties.getServerUrls());
//		clientFactory.setUserName(activeMQTTProperties.getUsername());
//		clientFactory.setPassword(activeMQTTProperties.getPassword());
//		clientFactory.setCleanSession(activeMQTTProperties.isCleanSession());
//		clientFactory.setConnectionTimeout(activeMQTTProperties.getConnectionTimeout());
//		clientFactory.setKeepAliveInterval(activeMQTTProperties.getKeepAliveInterval());
//		clientFactory.setSslProperties(activeMQTTProperties.getSsl());
//		clientFactory.setConsumerStopAction(activeMQTTProperties.getConsumerStopAction());
		
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(activeMQTTProperties.getServerUrls());
		if (activeMQTTProperties.getUsername()!=null) {
			options.setUserName(activeMQTTProperties.getUsername());
		}
		if (activeMQTTProperties.getPassword()!=null) {
			options.setPassword(activeMQTTProperties.getPassword().toCharArray());
		}
		options.setCleanSession(activeMQTTProperties.isCleanSession());
		options.setConnectionTimeout(activeMQTTProperties.getConnectionTimeout());
		options.setKeepAliveInterval(activeMQTTProperties.getKeepAliveInterval());
		options.setSSLProperties(activeMQTTProperties.getSsl());
		clientFactory.setConsumerStopAction(activeMQTTProperties.getConsumerStopAction());
		clientFactory.setConnectionOptions(options);
		
		return clientFactory;
	}

//	@ConditionalOnMissingBean(MqttMessageConverter.class)
//	@ConditionalOnProperty(value = ActiveMQTTProperties.MESSAGE_CONVERTER_KEY, havingValue = "json")
	@Bean
	public JsonPahoMessageConverter jsonPahoMessageConverter() {
		OutBoundClientProps client = activeMQTTProperties.getOutbound();
		return new JsonPahoMessageConverter(client.getDefaultQos(), client.isDefaultRetained());
	}

	static JsonPahoMessageConverter jsonPahoMessageConverter(int defaultQos, boolean defaultRetain) {
		JsonPahoMessageConverter converter = new JsonPahoMessageConverter(defaultQos, defaultRetain);
		try {
			converter.afterPropertiesSet();
		} catch (Exception e) {
			throw new BaseException("JsonPahoMessageConverter init error: " + e.getMessage(), e);
		}
		return converter;
	}
	
	@Configuration
	@ConditionalOnProperty(value = OutBoundClientProps.CLIENT_ID_KEY)
	protected static class OutboundConfiguration {
		@Autowired
		private ActiveMQTTProperties activeMQTTProperties;
		@Autowired
		ApplicationContext context;
//		@Value("${spring.application.name:''}")
//		private String defaultClientId;
		
		/***
		 * 将消息发送给为一个订阅者，然后阻碍发送直到消息被接收
		 * @author weishao zeng
		 * @return
		 */
		@Bean(name = Mqtts.OUTBOUND_CHANNEL)
		@ConditionalOnMissingBean(name = Mqtts.OUTBOUND_CHANNEL)
		public MessageChannel mqttOutboundChannel() {
			return new DirectChannel();
		}
		
		@Bean
		@ServiceActivator(inputChannel = Mqtts.OUTBOUND_CHANNEL)
		public MqttPahoMessageHandler mqttOutbound(MqttPahoClientFactory clientFactory, JsonPahoMessageConverter jsonPahoMessageConverter) {
			OutBoundClientProps client = activeMQTTProperties.getOutbound();
			String clientId = client.getClientId();
			Assert.hasText(clientId, "Outbound clientId can not be blank!");
			
			MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, clientFactory);
			handler.setAsync(client.isAsync());
			handler.setAsyncEvents(client.isAsyncEvents());
			handler.setDefaultQos(client.getDefaultQos());
			handler.setDefaultRetained(client.isDefaultRetained());
			handler.setCompletionTimeout(client.getCompletionTimeout());
			
			if (MessageConverters.JSON.equals(client.getConverter())) {
				JsonPahoMessageConverter converter = jsonPahoMessageConverter; //sonPahoMessageConverter(client.getDefaultQos(), client.isDefaultRetained());
				handler.setConverter(converter);
			}
			
			return handler;
		}
		
	}
	

	@Configuration
//	@ConditionalOnProperty(value = InBoundClientProps.ENABLED_KEY)
	protected static class InboundRegistarConfiguration implements InitializingBean {
		@Autowired
		private ActiveMQTTProperties activeMQTTProperties;
		@Autowired
		MqttPahoClientFactory clientFactory;
		
		@Autowired
		JsonPahoMessageConverter jsonPahoMessageConverter;
		
		@Autowired
		ApplicationContext context;
		@Autowired
		AsyncTaskExecutor asyncTaskExecutor;

		@Override
		public void afterPropertiesSet() throws Exception {
			List<InBoundClientProps> inbounds = activeMQTTProperties.getInbounds();
			if (inbounds.isEmpty()) {
				return ;
			}
			
			for (InBoundClientProps props : inbounds) {
				String channelName = props.getChannelName();
				if (StringUtils.isBlank(channelName)) {
					throw new BaseException("inbound output channel name can not blank!");
				}
				
				MqttPahoMessageDrivenChannel drivenChannel = new MqttPahoMessageDrivenChannel(props, clientFactory);

				if (MessageConverters.JSON.equals(props.getConverter())) {
					JsonPahoMessageConverter converter = jsonPahoMessageConverter;
					drivenChannel.setConverter(converter);
				}
//				MessageChannel channel = null;
//				if (!context.containsBean(channelName)) {
//					channel = SpringUtils.registerBean(context, channelName, DirectChannel.class);
//				} else {
//					channel = SpringUtils.getBean(context, channelName);
//				}
				//在构造函数和onInit函数设置了
//				drivenChannel.setOutputChannel(channel);
//				drivenChannel.setOutputChannelName(channelName);
//				drivenChannel.setQos(props.getQos());
//				drivenChannel.setCompletionTimeout(props.getCompletionTimeout());
				
				String beanName = channelName + "Adapter";
				SpringUtils.registerSingleton(context, beanName, drivenChannel);
				SpringUtils.initializeBean(context, drivenChannel);
			}
		}
		
	}
	
	
	/****
	 * 如果需要配置不同的消费者，复制参考此配置
	 * 
	 * @author way
	 *
	 */
//	@Configuration
//	@ConditionalOnProperty(value = InBoundClientProps.CLIENT_ID_KEY)
//	protected static class InboundConfiguration {
//		@Autowired
//		private ActiveMQTTProperties activeMQTTProperties;
////		@Value("${spring.application.name:''}")
////		private String defaultClientId;
//		
//		@Bean(name = Mqtts.INBOUND_CHANNEL)
//	    public MessageChannel mqttInputChannel() {
//	        return new DirectChannel();
//	    }
//		
//		@Bean
//		public MessageProducerSupport mqttInbound(MqttPahoClientFactory clientFactory, MqttMessageConverter converter) {
//			InBoundClientProps client = activeMQTTProperties.getInbound();
//
//			Assert.notEmpty(client.getTopics(), "Inbound topics can not be empty!");
//			
//			String clientId = client.getClientId();
//			Assert.hasText(clientId, "Inbound clientId can not be blank!");
//			
//			MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, clientFactory, client.getTopics());
//			adapter.setQos(client.getQos());
//			adapter.setCompletionTimeout(client.getCompletionTimeout());
////			adapter.setConverter(new DefaultPahoMessageConverter());
//			adapter.setConverter(converter);
//			adapter.setOutputChannel(mqttInputChannel());
//			
//			return adapter;
//		}
//	}
		
		// 配置多个 mqttInbound 相当于启动了不同的client实例，并且把接收到的消息写到了同一个mqttInputChannel，
		// 因此监听这个mqttInputChannel的handler会接收到两次消息
//		@Bean
//		public MessageProducerSupport mqttInbound2(MqttPahoClientFactory clientFactory, MqttMessageConverter converter) {
//			InBoundClientProps client = activeMQTTProperties.getInbound();
//
//			Assert.notEmpty(client.getTopics(), "Inbound topics can not be empty!");
//			
//			String clientId = client.getClientId() + "-test2";
//			Assert.hasText(clientId, "Inbound clientId can not be blank!");
//			
//			MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, clientFactory, client.getTopics());
//			adapter.setQos(client.getQos());
//			adapter.setCompletionTimeout(client.getCompletionTimeout());
////			adapter.setConverter(new DefaultPahoMessageConverter());
//			adapter.setConverter(converter);
//			adapter.setOutputChannel(mqttInputChannel());
//			
//			return adapter;
//		}
		
		
//		@Bean
//		@ServiceActivator(inputChannel = Mqtts.INBOUND_CHANNEL) // 绑定消费者
//		public MessageHandler handler() {
//			return new ReceiveMessageHandler();
//		}

}
