package org.onetwo.boot.module.activemq.mqtt.data;

import java.io.Serializable;
import java.util.Date;

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
public class ConvertedMessage<T> implements Serializable {
	String topic;
	T body;
	@JsonFormat(timezone = JsonMapper.TIME_ZONE_CHINESE, pattern = DateUtils.DATE_TIME_MILLS)
	Date receivedAt;
	
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
