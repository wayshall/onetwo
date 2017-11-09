package org.onetwo.ext.alimq;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
public class SimpleMessage {
	String key;
	String topic;
	String tags;
	Object body;
	Long delayTimeInMillis;
}
