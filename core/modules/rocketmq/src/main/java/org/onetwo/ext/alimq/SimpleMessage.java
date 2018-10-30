package org.onetwo.ext.alimq;

import java.util.Date;
import java.util.Properties;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.utils.StringUtils;

import com.aliyun.openservices.ons.api.Message;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@Data
@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessage implements OnsMessage {
	/***
	 * key代表消息的唯一标识，一般为事件名称+产生事件的领域模型（或者实体）标识+时间戳
	 * TODO: 可考虑自动生成
	 */
	private String key;
	private String topic;
	private String tags;
	private Object body;
	
//	private String eventName;
//	private String aggregationId;
//	private Date occurOn;
	
	// 下面是延时消息需要用到的字段
	
	private Long delayTimeInMillis;
	private Date deliverAt;
	private String deliverAtString;
    /**
     * 用户属性
     */
    private Properties userProperties;

    @Builder
	public SimpleMessage(String key, String topic, String tags, Object body, Long delayTimeInMillis, Date deliverAt,
			String deliverAtString, Properties userProperties) {
		super();
		this.key = key;
		this.topic = topic;
		this.tags = tags;
		this.body = body;
		this.delayTimeInMillis = delayTimeInMillis;
		this.deliverAt = deliverAt;
		this.deliverAtString = deliverAtString;
		if(userProperties!=null) {
			this.userProperties = userProperties;
		}
	}
    
    public SimpleMessage putUserProperty(String key, String value) {
    	if(this.userProperties==null) {
    		this.userProperties = new Properties();
    	}
    	this.userProperties.setProperty(key, value);
    	return this;
    }
    
	
	public Message toMessage(){
		Message message = new Message();
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
		message.setKey(key);
		return message;
	}


}
