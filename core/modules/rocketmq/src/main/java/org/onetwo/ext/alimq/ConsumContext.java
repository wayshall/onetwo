package org.onetwo.ext.alimq;

import java.util.Map;

import org.apache.rocketmq.common.message.MessageExt;

import com.google.common.collect.Maps;

import lombok.Builder;
import lombok.Data;

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
	@Builder.Default
	Map<String, Object> datas = Maps.newHashMap();
	String dataId;

	private boolean willSkipConsume;
	


	public boolean isWillSkipConsume() {
		return willSkipConsume;
	}

	public void markWillSkipConsume() {
		this.willSkipConsume = true;
	}
	
	public void setValue(String name, Object data) {
		this.datas.put(name, data);
	}
	
	public Object getValue(String name) {
		return this.datas.get(name);
	}
	
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
