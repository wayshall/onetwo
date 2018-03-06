package org.onetwo.ext.alimq;

import java.io.Serializable;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
public interface OnsMessage extends Serializable {
	Object getBody();
	Message toMessage();
}
