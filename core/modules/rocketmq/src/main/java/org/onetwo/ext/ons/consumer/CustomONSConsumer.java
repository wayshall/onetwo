package org.onetwo.ext.ons.consumer;

import org.onetwo.ext.alimq.ConsumContext;


public interface CustomONSConsumer {

	void doConsume(ConsumContext consumContext);
	
	default Class<?> getMessageBodyClass(ConsumContext consumContext) {
		return null;
	}
}
