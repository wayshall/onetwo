package org.onetwo.boot.module.activemq.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;

/**
 * @author weishao zeng
 * <br/>
 */

public class JsonPahoMessageConverter extends DefaultPahoMessageConverter implements InitializingBean {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	private JsonMapper jsonMapper;

	public JsonPahoMessageConverter(int defaultQos, boolean defaultRetain) {
		super(defaultQos, defaultRetain);
		this.setPayloadAsBytes(true);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.jsonMapper = Mqtts.getJsonMapper();
	}

	@Override
	protected byte[] messageToMqttBytes(Message<?> message) {
		Object payload = message.getPayload();
		if (!(payload instanceof byte[] || payload instanceof String)) {
			byte[] data = jsonMapper.toJsonBytes(payload);
			return data;
		}
		
		return super.messageToMqttBytes(message);
	}
	

	@Override
	protected Object mqttBytesToPayload(MqttMessage mqttMessage) {
		try {
			Object payload = jsonMapper.fromJson(mqttMessage.getPayload());
			return payload;
		} catch (Exception e) {
			logger.error("parse mqtt payload error: " + e.getMessage(), e);
		}
		return super.mqttBytesToPayload(mqttMessage);
	}
}
