package org.onetwo.ext.alimq;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wayshall
 * <br/>
 */
public interface OnsMessage extends Serializable {
	Object getBody();
	ExtMessage toMessage();
	
	public interface TracableMessage {
		String PREFIX = "__jfish_";

		String USER_ID_KEY = PREFIX + "user_id";
		String DATA_ID_KEY = PREFIX + "data_id";
		String OCCUR_ON_KEY = PREFIX + "occur_on";
		String SERIALIZER_KEY = PREFIX + "serializer";
		String DEBUG_KEY = PREFIX + "debug";
//		String IDENTITY_KEY = PREFIX + "identity";
		
		String getUserId();
		void setUserId(String userId);

		String getDataId();
		void setDataId(String dataId);

		Date getOccurOn();
		
		void setOccurOn(Date occurOn);
		
		void setIdentityKey(String identityKey);
		String getIdentityKey();
		
		/****
		 * @see org.onetwo.ext.ons.ONSProperties.MessageSerializerType
		 * @param serializer
		 */
		void setSerializer(String serializer);
		String getSerializer();

		
		void setDebug(boolean debug);
		boolean isDebug();
		
		/***
		 * 延迟消息级别
		 * @return
		 */
		Integer getDelayTimeLevel();
		
	}
	
}
