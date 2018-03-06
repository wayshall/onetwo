package org.onetwo.boot.module.activemq;

import java.io.Serializable;

import javax.jms.Destination;

import lombok.Builder;
import lombok.Data;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.onetwo.boot.module.jms.JmsMessage;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
public class ActivemqMessage implements JmsMessage {
	/**
	 * @author wayshall
	 * 
	 */
	private static final long serialVersionUID = 3802471292109034362L;
	
	private String destinationName;
	private DesinationType type = DesinationType.QUEUE;
	private Serializable messageBody;
	

	@Override
	public Destination getDestination() {
		Destination dest = null;
		if(getType()==DesinationType.QUEUE){
			dest = new ActiveMQQueue(destinationName);
		}else if(getType()==DesinationType.TOPIC){
			dest = new ActiveMQTopic(destinationName);
		}else{
			throw new IllegalArgumentException();
		}
		return dest;
	}
	
	public static enum DesinationType {
		QUEUE,
		TOPIC
	}


}
