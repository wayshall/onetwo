package org.onetwo.ext.alimq;

import org.onetwo.ext.ons.consumer.ConsumerMeta;



/**
 * @author wayshall
 * <br/>
 */
public interface ConsumerListener {

	void beforeConsumeMessage(ConsumerMeta consumerMeta, ConsumContext context);
	void afterConsumeMessage(ConsumerMeta consumerMeta, ConsumContext context);
	void onConsumeMessageError(ConsumContext context, Throwable e);
	
	default void onBatchConsumeMessageError(BatchConsumContext context, Throwable e) {
		onConsumeMessageError(context.getCurrentContext(), e);
	}

}
