package org.onetwo.ext.alimq;

import com.aliyun.openservices.ons.api.SendResult;


/**
 * @author wayshall
 * <br/>
 */
public interface ProducerListener<T> {

	void beforeSendMessage(T message);
	void afterSendMessage(T message, SendResult sendResult);
	void onSendMessageError(T message, Throwable throable);

}
