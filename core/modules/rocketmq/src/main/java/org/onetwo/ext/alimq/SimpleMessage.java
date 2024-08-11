package org.onetwo.ext.alimq;

import java.util.Date;
import java.util.Properties;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.alimq.OnsMessage.TracableMessage;

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
	
	/***
	 * 基于database实现的延迟消息
	 */
	// 下面是延时消息需要用到的字段
	private Long delayTimeInMillis;
	private Date deliverAt;
	private String deliverAtString;
	
	/****
	 * rocketmqy原生延迟消息
	 * @see {@link com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.Message#setDelayTimeLevel(int)}
	 * MessageConst.PROPERTY_DELAY_TIME_LEVEL
	 * 
	 * 默认支持18个level的延迟消息，这是通过broker端的messageDelayLevel配置项确定的，如下：
	 * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
	 * 
	 * level == 0，消息为非延迟消息
1<=level<=maxLevel，消息延迟特定时间，例如level==1，延迟1s
level > maxLevel，则level== maxLevel，例如level==20，延迟2
	 */
	private Integer delayTimeLevel;
    /**
     * 用户属性
     */
    private Properties userProperties;
    
    /***
     * 指定客户端的反序列化器
     */
    private String serializer;
    
    private boolean debug;
    private ExtMessage message;

    public SimpleMessage putUserProperty(String key, String value) {
    	if(this.userProperties==null) {
    		this.userProperties = new Properties();
    	}
    	this.userProperties.setProperty(key, value);
    	return this;
    }
    
	
	public ExtMessage toMessage(){
		if (message!=null) {
			return message;
		}
//		Message message = new Message();
		ExtMessage message = new ExtMessage();
		message.setTopic(topic);
		message.setTags(tags);
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
		message.setKeys(key);
		message.setDelayTimeLevel(delayTimeLevel);
		
		this.message = message;
		
		return message;
	}


}
