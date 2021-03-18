package org.onetwo.boot.module.activemq.mqtt.data;

import java.io.Serializable;

import org.onetwo.common.jackson.JsonMapper;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
@Data
public class ConvertedMessage<T> implements Serializable {
	String topic;
	T body;
	public ConvertedMessage(String topic, T body) {
		super();
		this.topic = topic;
		this.body = body;
	}
	
	public String bodyAsJson() {
		if (body==null) {
			return "";
		}
		return JsonMapper.toJsonString(body);
	}
}
