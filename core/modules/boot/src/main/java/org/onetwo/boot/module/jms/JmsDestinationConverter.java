package org.onetwo.boot.module.jms;

import javax.jms.Destination;

/**
 * @author wayshall
 * <br/>
 */
public interface JmsDestinationConverter {
	
	Destination getDestination(JmsMessageCreator jmsMessage);

}
