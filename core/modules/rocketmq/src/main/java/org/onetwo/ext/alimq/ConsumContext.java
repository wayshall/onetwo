package org.onetwo.ext.alimq;

import lombok.Builder;
import lombok.Data;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
public class ConsumContext {
	String messageId;
	MessageExt message;
	Object deserializedBody;
}
