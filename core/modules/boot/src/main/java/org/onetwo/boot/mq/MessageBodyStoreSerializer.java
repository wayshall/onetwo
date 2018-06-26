package org.onetwo.boot.mq;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;



public interface MessageBodyStoreSerializer {

	byte[] serialize(Serializable message);
	Serializable deserialize(byte[] body);
	
	MessageBodyStoreSerializer DEFAULT = new Default(); 
	
	public class Default implements MessageBodyStoreSerializer {

		@Override
		public byte[] serialize(Serializable message) {
			return SerializationUtils.serialize(message);
		}

		@Override
		public Serializable deserialize(byte[] body) {
			return SerializationUtils.deserialize(body);
		}
		
	}

}
