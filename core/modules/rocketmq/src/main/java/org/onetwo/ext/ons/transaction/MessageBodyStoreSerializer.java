package org.onetwo.ext.ons.transaction;

import org.apache.commons.lang3.SerializationUtils;

import com.aliyun.openservices.ons.api.Message;


public interface MessageBodyStoreSerializer {

	byte[] serialize(Message message);
	Message deserialize(byte[] body);
	
	MessageBodyStoreSerializer INSTANCE = new Default();
	
	public class Default implements MessageBodyStoreSerializer {

		@Override
		public byte[] serialize(Message message) {
			return SerializationUtils.serialize(message);
		}

		@Override
		public Message deserialize(byte[] body) {
			return SerializationUtils.deserialize(body);
		}
		
	}

}
