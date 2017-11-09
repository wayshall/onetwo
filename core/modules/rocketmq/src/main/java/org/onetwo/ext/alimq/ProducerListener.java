package org.onetwo.ext.alimq;


/**
 * @author wayshall
 * <br/>
 */
public interface ProducerListener<T> {

	void beforeSendMessage(T message);
	void onSendMessageError(T message, Throwable throable);

}
