package org.onetwo.ext.alimq;

public interface MessageSerializer {
	
	public byte[] serialize(Object message);

}
