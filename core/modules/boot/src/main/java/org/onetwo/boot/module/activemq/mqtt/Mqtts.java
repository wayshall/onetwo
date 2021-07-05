package org.onetwo.boot.module.activemq.mqtt;

import java.util.LinkedHashMap;

import org.onetwo.boot.module.activemq.mqtt.data.ConvertedMessage;
import org.onetwo.boot.module.activemq.mqtt.data.LinkedMapMessage;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
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
	private static final JsonMapper typingJsonMapper = JsonMapper.ignoreNull().enableTyping();
	private static final JsonMapper jsonMapper = JsonMapper.ignoreNull();
	
	public static String getTopic(Message<?> message) {
		Assert.notNull(message, "message can not be null");
		String topic = (String)message.getHeaders().get(MqttHeaders.TOPIC);
		if (StringUtils.isBlank(topic)) {
			topic = (String)message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
		}
		return topic;
	}
	
	/***
	 * json数据带有类型信息，即TypingJson
	 * @author weishao zeng
	 * @param message
	 * @param type
	 * @return
	 */
	public static <T> T convertPayload(Message<?> message, Class<T> type) {
		return convertPayloadWithTypeingJson(message, type);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T convertPayloadWithTypeingJson(Message<?> message, Class<T> type) {
		Assert.notNull(message, "message can not be null");
		try {
			T data = (T) typingJsonMapper.fromJson(message.getPayload(), type);
			return data;
		} catch (Exception e) {
			JFishLoggerFactory.getCommonLogger().error("convert payload error!",e);
		}
		return null;
	}

	/****
	 * 普通到json数据，不包含类信息
	 * @author weishao zeng
	 * @param message
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertPayloadWithJson(Message<?> message, Class<T> type) {
		Assert.notNull(message, "message can not be null");
		try {
			T data = (T) jsonMapper.fromJson(message.getPayload(), type);
			return data;
		} catch (Exception e) {
			JFishLoggerFactory.getCommonLogger().error("convert payload error!",e);
		}
		return null;
	}
	

	public static <T> ConvertedMessage<T> convertedMessage(Message<?> message, Class<T> bodyClass) {
		String topic = Mqtts.getTopic(message);
		T body = Mqtts.convertPayloadWithJson(message, bodyClass);
		ConvertedMessage<T> msg = new ConvertedMessage<>(topic, body);
		return msg;
	}
	

	@SuppressWarnings("unchecked")
	public static LinkedMapMessage linkedMapMessage(Message<?> message) {
		String topic = Mqtts.getTopic(message);
		LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>)Mqtts.convertPayloadWithJson(message, LinkedHashMap.class);
		LinkedMapMessage msg = new LinkedMapMessage(topic, body);
		return msg;
	}
	
	static JsonMapper getJsonMapper() {
		return jsonMapper;
	}
	
	static JsonMapper getTypingJsonMapper() {
		return typingJsonMapper;
	}

	private Mqtts() {
	}
}
