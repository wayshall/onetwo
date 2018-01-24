package org.onetwo.ext.ons.producer;
/**
 * @author wayshall
 * <br/>
 */
public interface TraceableProducer {

	/***
	 * ProducerBean or TransactionProducerBean
	 * @author wayshall
	 * @param targetClass
	 * @return
	 */
	<T> T getRawProducer(Class<T> targetClass);
	
	/***
	 * 是否事务的producer
	 * @author wayshall
	 * @return
	 */
	boolean isTransactional();
	
}
