package org.onetwo.ext.rocketmq.producer;

public interface MessageSerializer {
	
	public byte[] serialize(Object message);

}
