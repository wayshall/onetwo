package org.onetwo.boot.module.jms;

import java.io.Serializable;

import javax.jms.Destination;

/**
 * @author wayshall
 * <br/>
 */
public interface JmsMessage extends Serializable {
	
	Object getMessageBody();
	Destination getDestination();

}
