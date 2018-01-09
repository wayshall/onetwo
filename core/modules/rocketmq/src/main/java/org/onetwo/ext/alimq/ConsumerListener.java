package org.onetwo.ext.alimq;



/**
 * @author wayshall
 * <br/>
 */
public interface ConsumerListener {

	void beforeConsumeMessage(ConsumContext context);
	void afterConsumeMessage(ConsumContext context);
	void onConsumeMessageError(ConsumContext context, Throwable e);

}
