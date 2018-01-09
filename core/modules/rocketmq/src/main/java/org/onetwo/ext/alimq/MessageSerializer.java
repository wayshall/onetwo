package org.onetwo.ext.alimq;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

import com.aliyun.openservices.ons.api.Message;

public interface MessageSerializer {
	
	public byte[] serialize(Object body, MessageDelegate messageDelegate);
	
	MessageSerializer DEFAULT = (body, messageDelegate)->SerializationUtils.serialize((Serializable)body);
	
	public class MessageDelegate {
		private final Message message;

		public MessageDelegate(Message message) {
			super();
			this.message = message;
		}

		public void putUserProperties(String key, String value) {
			message.putUserProperties(key, value);
		}

		public String getUserProperties(String key) {
			return message.getUserProperties(key);
		}
	}
}
