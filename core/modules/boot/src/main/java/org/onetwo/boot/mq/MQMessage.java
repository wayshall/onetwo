package org.onetwo.boot.mq;


/**
 * @author wayshall
 * <br/>
 */
public interface MQMessage<M> {
	Object getBody();
	M toMessage();
}
