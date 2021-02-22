package org.onetwo.boot.module.activemq.mqtt.data;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.onetwo.common.jackson.JsonMapper;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
@Data
public class LinkedMapMessage implements Serializable {
	String topic;
	LinkedHashMap<String, Object> body;
	public LinkedMapMessage(String topic, LinkedHashMap<String, Object> body) {
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
