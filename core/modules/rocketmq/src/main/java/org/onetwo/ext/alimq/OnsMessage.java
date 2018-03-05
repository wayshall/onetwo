package org.onetwo.ext.alimq;

import org.onetwo.boot.mq.MQMessage;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
public interface OnsMessage extends MQMessage<Message> {
}
