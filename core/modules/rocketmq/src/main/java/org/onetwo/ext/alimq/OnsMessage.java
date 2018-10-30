package org.onetwo.ext.alimq;

import java.io.Serializable;
import java.util.Date;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
public interface OnsMessage extends Serializable {
	Object getBody();
	Message toMessage();
	
	public interface TracableMessage {
		
		String getUserId();
		void setUserId(String userId);

		String getDataId();
		void setDataId(String dataId);

		Date getOccurOn();
		
		void setOccurOn(Date occurOn);
		
	}
	
}
