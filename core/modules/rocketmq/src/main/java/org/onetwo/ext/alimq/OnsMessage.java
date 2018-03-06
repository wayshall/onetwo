package org.onetwo.ext.alimq;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
public interface OnsMessage {
	Object getBody();
	Message toMessage();
}
