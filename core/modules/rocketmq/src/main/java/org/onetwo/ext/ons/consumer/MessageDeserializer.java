package org.onetwo.ext.ons.consumer;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
public interface MessageDeserializer {

	Object deserialize(MessageExt msg);
}
