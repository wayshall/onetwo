package org.onetwo.boot.module.activemq;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.onetwo.boot.module.jms.JmsDestinationConverter;
import org.onetwo.boot.module.jms.JmsMessageCreator;
import org.onetwo.boot.module.jms.JmsMessageCreator.DesinationType;

/**
 * @author wayshall
 * <br/>
 */
public class ActivemqDestinationConverter implements JmsDestinationConverter {

	@Override
	public Destination getDestination(JmsMessageCreator jmsMessage) {
		DesinationType desinationType = jmsMessage.getDesinationType();
		Destination dest = null;
		if(desinationType==DesinationType.QUEUE){
			dest = new ActiveMQQueue(jmsMessage.getDestinationName());
		}else if(desinationType==DesinationType.TOPIC){
			dest = new ActiveMQTopic(jmsMessage.getDestinationName());
		}else{
			throw new IllegalArgumentException();
		}
		return dest;
	}
	
	

}
