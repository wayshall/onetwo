package org.onetwo.boot.module.activemq.mqtt;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */

final public class Mqtts {
	
	public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";
//	public static final String INBOUND_CHANNEL = "mqttInboundChannel";
	
	public static String getTopic(Message<?> message) {
		Assert.notNull(message, "message can not be null");
		String topic = (String)message.getHeaders().get(MqttHeaders.TOPIC);
		return topic;
	}

	private Mqtts() {
	}
}
