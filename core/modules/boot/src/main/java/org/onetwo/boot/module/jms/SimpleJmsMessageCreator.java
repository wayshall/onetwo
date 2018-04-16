package org.onetwo.boot.module.jms;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class SimpleJmsMessageCreator implements JmsMessageCreator {
	/**
	 * @author wayshall
	 * 
	 */
	private static final long serialVersionUID = 3802471292109034362L;
	
	private String destinationName;
	private DesinationType desinationType;
	private JmsMessage<Serializable> jmsMessage;

	@Builder
	public SimpleJmsMessageCreator(String destinationName, DesinationType desinationType, String key, Serializable body) {
		super();
		this.destinationName = destinationName;
		this.desinationType = desinationType==null?DesinationType.QUEUE:desinationType;
		this.jmsMessage = new JmsMessage<>(key, body);
	}
	
}
