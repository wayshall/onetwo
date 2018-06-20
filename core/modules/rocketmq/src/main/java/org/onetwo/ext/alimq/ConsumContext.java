package org.onetwo.ext.alimq;

import lombok.Builder;
import lombok.Data;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
public class ConsumContext {
	String messageId;
	MessageExt message;
	Object deserializedBody;
	MessageDeserializer messageDeserializer;
	
	@SuppressWarnings("unchecked")
	public <T> T getDeserializedBody(){
		return (T) deserializedBody;
	}
	
	public String getTopic(){
		return message.getTopic();
	}
	
	public String getTags(){
		return message.getTags();
	}
}
