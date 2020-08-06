package org.onetwo.boot.module.activemq.mqtt;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
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
	private static final JsonMapper jsonMapper = JsonMapper.ignoreNull().enableTyping();
	
	public static String getTopic(Message<?> message) {
		Assert.notNull(message, "message can not be null");
		String topic = (String)message.getHeaders().get(MqttHeaders.TOPIC);
		return topic;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T convertPayload(Message<?> message, Class<T> type) {
		Assert.notNull(message, "message can not be null");
		try {
			T data = (T) jsonMapper.fromJson(message.getPayload(), type);
			return data;
		} catch (Exception e) {
			JFishLoggerFactory.getCommonLogger().error("convert payload error!",e);
		}
		return null;
	}

	static JsonMapper getJsonMapper() {
		return jsonMapper;
	}

	private Mqtts() {
	}
}
