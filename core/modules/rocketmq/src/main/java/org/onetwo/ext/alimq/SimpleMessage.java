package org.onetwo.ext.alimq;

import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessage {
	
	String key;
	String topic;
	String tags;
	Object body;
	Long delayTimeInMillis;
    /**
     * 用户属性
     */
    Properties userProperties;
	
	public Message toMessage(){
		Message message = new Message();
		message.setKey(key);
		message.setTopic(topic);
		message.setTag(tags);
		if(delayTimeInMillis!=null){
			message.setStartDeliverTime(System.currentTimeMillis()+delayTimeInMillis);
		}
		message.setUserProperties(userProperties);
		return message;
	}
}
