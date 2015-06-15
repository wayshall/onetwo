package org.onetwo.plugins.zkclient.curator;

public interface DataSerializer {
	
	public byte[] serialize(Object data);
	
	public <T> T deserialize(byte[] data, Class<T> objectClass);

}
