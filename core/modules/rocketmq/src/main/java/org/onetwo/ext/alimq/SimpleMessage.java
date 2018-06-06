package org.onetwo.ext.alimq;

import java.util.Date;
import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.utils.StringUtils;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessage implements OnsMessage {
	
	String key;
	String topic;
	String tags;
	Object body;
	Long delayTimeInMillis;
	Date deliverAt;
	String deliverAtString;
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
			this.delayTimeInMillis = System.currentTimeMillis()+delayTimeInMillis;
			message.setStartDeliverTime(delayTimeInMillis);
		}else if(deliverAt!=null){
			delayTimeInMillis = deliverAt.getTime();
			message.setStartDeliverTime(delayTimeInMillis);
		}else if(StringUtils.isNotBlank(deliverAtString)){
			delayTimeInMillis = DateUtils.parse(deliverAtString).getTime();
			message.setStartDeliverTime(delayTimeInMillis);
		}
		message.setUserProperties(userProperties);
		return message;
	}
}
