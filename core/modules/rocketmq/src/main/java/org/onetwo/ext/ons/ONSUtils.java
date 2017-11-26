package org.onetwo.ext.ons;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
public class ONSUtils {
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

}
