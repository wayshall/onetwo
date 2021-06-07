package org.onetwo.ext.ons.consumer;

import java.util.List;

import org.onetwo.ext.alimq.ConsumContext;


public interface CustomONSConsumer {

	void doConsume(ConsumContext consumContext);
	void doConsumeBatch(List<ConsumContext> batchContexts);
	
	default Class<?> getMessageBodyClass(ConsumContext consumContext) {
		return null;
	}
}
