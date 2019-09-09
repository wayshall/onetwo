package org.onetwo.ext.alimq;

import java.util.Date;
import java.util.Properties;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.alimq.OnsMessage.TracableMessage;

import com.aliyun.openservices.ons.api.Message;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessage implements OnsMessage, TracableMessage {
	/***
	 * key代表消息的唯一标识，一般为64位，规则为:事件名称(topic>tag).操作的用户id(36进制).产生事件的领域模型（或者实体）标识.时间戳(36进制毫秒)
	 * 注意，事件名称后面的属性加起来的长度一般为41，所以主要topic和tag的长度
	 */
	private String key;
	private String identityKey;
	private String topic;
	private String tags;
	private Object body;
//	private DomainEvent event; // topic and tag
	
	// 下面属性主要为了生成key，暂时不发送到客户端
	/***
	 * 操作用户，，可为null
	 */
	private String userId;
	/***
	 * 聚合id，一般不为空
	 */
	private String dataId;
	/***
	 * 产生的时间，可为null
	 */
	private Date occurOn;
	
	// 下面是延时消息需要用到的字段
	
	private Long delayTimeInMillis;
	private Date deliverAt;
	private String deliverAtString;
    /**
     * 用户属性
     */
    private Properties userProperties;
    
    /***
     * 指定客户端的反序列化器
     */
    private String serializer;

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
