package org.onetwo.ext.alimq;

import lombok.Builder;
import lombok.Data;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
public class SimpleMessage {
	String key;
	String topic;
	String tags;
	Object body;
	Long delayTimeInMillis;
	
	public Message toMessage(){
		Message message = new Message();
		message.setKey(key);
		message.setTopic(topic);
		message.setTag(tags);
		if(delayTimeInMillis!=null){
			message.setStartDeliverTime(System.currentTimeMillis()+delayTimeInMillis);
		}
		return message;
	}
}
