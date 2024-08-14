package org.onetwo.boot.module.jms;

import java.io.Serializable;

/**
 * @author wayshall
 * <br/>
 */
public interface JmsMessageCreator extends Serializable {
	
	String getDestinationName();
	JmsMessage<? extends Serializable> getJmsMessage();
	DesinationType getDesinationType();
	
	
	public static enum DesinationType {
		QUEUE,
		TOPIC
	}

}
