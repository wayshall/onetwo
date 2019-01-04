package org.onetwo.ext.ons;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.ext.alimq.OnsMessage.TracableMessage;
import org.slf4j.Logger;

import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
final public class ONSUtils {
	private final static int MAX_KEY_LENGTH = 128;
	
	public static final SendResult ONS_SUSPEND;
	static {
		SendResult res = new SendResult();
		res.setMessageId("SUSPEND");
		ONS_SUSPEND = res;
	}
	
	public static final String LOGGER_NAME = "org.onetwo.ext.ons.ONSMessageLog";
	
	public static Logger getONSLogger(){
		return JFishLoggerFactory.getLogger(LOGGER_NAME);
	}
	
	public static String getMessageId(MessageExt message){
		return message.getProperty(MessageConst.PROPERTY_UNIQ_CLIENT_MESSAGE_ID_KEYIDX);
	}

	public static long getMessageDiff(MessageExt msg){
		try {
			long offset = msg.getQueueOffset();//消息自身的offset
			String maxOffset = msg.getProperty(MessageConst.PROPERTY_MAX_OFFSET);//当前最大的消息offset
			long diff = Long.parseLong(maxOffset)-offset;//消费当前消息时积压了多少消息未消费
			return diff;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/***
	 * key代表消息的唯一标识，长度为 {@value ONSUtils#MAX_KEY_LENGTH} ，规则为:事件名称(topic>tag).操作的用户id(36进制).产生事件的领域模型（或者实体）标识.时间戳(36进制毫秒)
	 * 注意，事件名称后面的属性加起来的长度一般为41，所以注意topic和tag的长度
	 * 
	 * identityKey 标识某用户某操作的身份key，不带时间戳，结合时间戳，可给客户端用于判断某类操作由于消息无序而带来的问题，
	 * 比如同一类操作，a先发生，b后发生，但是b先到达，理论上后到达的a不应该执行
	 * @author weishao zeng
	 * @param topic
	 * @param tag
	 * @param domainEvent
	 * @return
	 */
	public static TracableMessageKey toKey(String topic, String tag, TracableMessage tracableMessage) {
		String defaultId = "0";
		Date occurOn = tracableMessage.getOccurOn()==null?new Date():tracableMessage.getOccurOn();
		String eventName = topic;
		if(StringUtils.isNotBlank(tag)) {
			eventName = eventName + ">" + tag;
		}
		String userId = tracableMessage.getUserId();
		try {
			// 如果是数字，则压缩userId，避免key太长
			if (userId!=null) {
				userId = Long.toString(Long.parseLong(userId), 36);
			} else {
				userId = defaultId;
			}
		} catch (Exception e) {
			// ignore
		}
		String identityKey = eventName + "." + userId + "." + (tracableMessage.getDataId()!=null?tracableMessage.getDataId():defaultId);
		String key = identityKey + "." + Long.toString(occurOn.getTime(), 36);
		if(key.length()>MAX_KEY_LENGTH) {
			throw new BaseException("message key is too long, can not more than " + MAX_KEY_LENGTH + " : " + key);
		}
		return TracableMessageKey.builder()
								.identityKey(identityKey)
								.key(key)
								.build();
	}
	
}
