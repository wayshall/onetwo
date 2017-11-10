package org.onetwo.ext.alimq;

import org.apache.commons.lang3.SerializationUtils;


public interface MessageDeserializer {
	
	Object deserialize(byte[] body);

	MessageDeserializer DEFAULT = body->SerializationUtils.deserialize(body);
}
