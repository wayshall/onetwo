package org.onetwo.ext.ons.consumer;

import org.onetwo.ext.alimq.ConsumContext;


public interface CustomONSConsumer<T> {

	void doConsume(ConsumContext consumContext);
	
}
