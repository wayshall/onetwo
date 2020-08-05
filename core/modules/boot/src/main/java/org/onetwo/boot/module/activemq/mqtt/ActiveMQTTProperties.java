package org.onetwo.boot.module.activemq.mqtt;

import java.util.Map;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.onetwo.boot.module.activemq.ActivemqProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.integration.mqtt.core.ConsumerStopAction;

import com.google.common.collect.Maps;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
@ConfigurationProperties(value=ActiveMQTTProperties.PREFIX_KEY)
public class ActiveMQTTProperties {
	public static final String PREFIX_KEY = ActivemqProperties.PREFIX_KEY + ".mqtt";
	public static final String MESSAGE_CONVERTER_KEY = PREFIX_KEY + ".messageConverter";
	
	/***
	 * jfish.activemq.mqtt.server-urls
	 */
	public static final String SERVER_URLS_KEY = PREFIX_KEY + ".server-urls";
	
	String[] serverUrls;
	
	/***
	 * conf/credentials.properties
	 */
	String username;
	String password;
	
	/***
	 * 设置客户端的默认停止行为：ConsumerStopAction.UNSUBSCRIBE_CLEAN
	 * 当客户端适配器停止时，如果cleanSession设置为true时，client会取消订阅。
	 * 这样，当重新连接的时候，断开期间收到的消息将不会发送给订阅者。
	 * 如果不想这样，推荐设置为UNSUBSCRIBE_NEVER, UNSUBSCRIBE_NEVER=UNSUBSCRIBE_CLEAN+cleanSession(false)
	 */
	ConsumerStopAction consumerStopAction = ConsumerStopAction.UNSUBSCRIBE_CLEAN;
	/***
	 * 
设置客户端和服务器是否应该记住重启和重新连接时的状态。
-如果设置为false，客户端和服务器将在客户端、服务器和连接的重启过程中保持状态。随着状态的维护。
◦即使客户端、服务器或连接重新启动，消息传递也会可靠地满足指定的QOS。
服务器将把订阅视为持久。

-如果设置为 "true"，客户端和服务器将不会在客户端、服务器或连接重新启动时保持状态。这意味着： -如果设置为 "true"，客户端和服务器将不会在客户端、服务器或连接的重启过程中保持状态。
 如果重新启动客户端、服务器或连接，则无法维持向指定QOS的信息传递。
服务器将把订阅视为非持久性的。
@see MqttConnectOptions.CLEAN_SESSION_DEFAULT
	 */
	boolean cleanSession = MqttConnectOptions.CLEAN_SESSION_DEFAULT;
	
	/***
	 * 设置连接超时值。 该值以秒为单位，定义了客户端等待与MQTT服务器建立网络连接的最大时间间隔。
 默认的超时值是30秒。
值为0会禁用超时处理，这意味着客户端将等待网络连接建立成功或失败。
	 */
	int connectionTimeout = MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;
	
	/***
	 * 设置 "保持活力 "的时间间隔。
 该值以秒为单位，定义了发送或接收消息之间的最大时间间隔。它使客户机能够检测服务器是否不再可用，而不需要
  不得不等待TCP/IP超时。客户端将确保
  在每个保持期内至少有一条消息在网络上传播。 如果在该时间段内没有数据相关的消息，客户端会发送一个非常小的 "ping "消息，服务器会确认。
 值为0则禁用客户端的keepalive处理。
	 */
	int keepAliveInterval = MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT;
	
	Properties ssl;
	
	
	OutBoundClientProps outbound = new OutBoundClientProps();
//	InBoundClientProps inbound = new InBoundClientProps();
	Map<String, InBoundClientProps> inbounds = Maps.newHashMap();
	
	@Data
	public static class OutBoundClientProps {
		public static final String CLIENT_ID_KEY = ActiveMQTTProperties.PREFIX_KEY + ".outbound.clientId";
		
		String clientId;
		/***
		 * 发布消息时，是否异步，默认为false，发送将会阻塞，直到确认发送
		 */
		boolean async = false;
		/***
		 * 当async和async事件(async-events)都为true时，将发出MqttMessageSentEvent。
		 * 它包含消息、主题、客户端库生成的消息id、clientId和clientInstance（每次连接客户端时递增）。
		 * 
		 * 当客户端库确认传递时，将发出MqttMessageDeliveredEvent。
		 * 它包含messageId、clientId和clientInstance，使传递与发送相关。任何ApplicationListener或事件入站通道适配器都可以接收这些事件。
		 * 
		 * 请注意，MqttMessageDeliveredEvent可能在MqttMessageSentEvent之前收到。默认值为false
		 */
		boolean asyncEvents = false;
		
		/***
		 * 最多一次（0）
			最少一次（1）
			只一次（2）
		 */
		int defaultQos = 1;
		
		/***
		 * Retained 消息是指在 PUBLISH 数据包中 Retain 标识设为 1 的消息，Broker 收到这样的 PUBLISH 包以后，将保存这个消息，当有一个新的订阅者订阅相应主题的时候，Broker 会马上将这个消息发送给订阅者
		 */
		boolean defaultRetained;
		
		/***
		 * 同步发送时（async=false），等待完成的超时时间，默认30秒
		 */
		int completionTimeout = 30000;
		MessageConverters converter;
	}
	

	@Data
	public static class InBoundClientProps {
//		public static final String ENABLED_KEY = ActiveMQTTProperties.PREFIX_KEY + ".inbounds";
//		public static final String CLIENT_ID_KEY = ActiveMQTTProperties.PREFIX_KEY + ".inbound.clientId";
		
		String clientId;
		
		String[] topics;

		/***
		 * 最多一次（0）
			最少一次（1）
			只一次（2）
		 */
		int[] qos = new int[] {1};
		
		/***
		 * 默认30秒
		 */
		int completionTimeout = 30000;
		
	}
	
	public static enum MessageConverters {
		JSON
	}

}
