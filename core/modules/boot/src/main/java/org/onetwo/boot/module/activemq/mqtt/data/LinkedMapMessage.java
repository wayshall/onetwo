package org.onetwo.boot.module.activemq.mqtt.data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.jackson.JsonMapper;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(timezone = JsonMapper.TIME_ZONE_CHINESE, pattern = DateUtils.DATE_TIME_MILLS)
	Date receivedAt;

	public LinkedMapMessage() {
	}
	
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
