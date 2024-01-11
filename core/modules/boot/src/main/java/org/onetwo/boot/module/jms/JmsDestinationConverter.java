package org.onetwo.boot.module.jms;

import jakarta.jms.Destination;

/**
 * @author wayshall
 * <br/>
 */
public interface JmsDestinationConverter {
	
	Destination getDestination(JmsMessageCreator jmsMessage);

}
