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
	Date deliverTime;
	String deliverTimeString;
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
		}else if(deliverTime!=null){
			delayTimeInMillis = deliverTime.getTime();
			message.setStartDeliverTime(System.currentTimeMillis()+delayTimeInMillis);
		}else if(StringUtils.isNotBlank(deliverTimeString)){
			delayTimeInMillis = DateUtils.parse(deliverTimeString).getTime();
			message.setStartDeliverTime(System.currentTimeMillis()+delayTimeInMillis);
		}
		message.setUserProperties(userProperties);
		return message;
	}
}
