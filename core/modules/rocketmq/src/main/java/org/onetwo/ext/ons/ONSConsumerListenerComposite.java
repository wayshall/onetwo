package org.onetwo.ext.ons;

import java.util.Collections;
import java.util.List;

import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.ConsumerListener;
import org.onetwo.ext.ons.consumer.ConsumerMeta;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/**
 * @author wayshall
 * <br/>
 */
public class ONSConsumerListenerComposite implements InitializingBean, ConsumerListener {
	
	@Autowired(required=false)
	private List<ConsumerListener> listeners;
	
	public ONSConsumerListenerComposite() {
		super();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.listeners = listeners==null?Collections.emptyList():listeners;
		AnnotationAwareOrderComparator.sort(listeners);
	}

	@Override
	public void beforeConsumeMessage(ConsumerMeta consumerMeta, ConsumContext context) {
		for(ConsumerListener listener : listeners){
			listener.beforeConsumeMessage(consumerMeta, context);
		}
	}

	@Override
	public void afterConsumeMessage(ConsumerMeta consumerMeta, ConsumContext context) {
		for(ConsumerListener listener : listeners){
			listener.afterConsumeMessage(consumerMeta, context);
		}
	}

	@Override
	public void onConsumeMessageError(ConsumContext context, Throwable e) {
		for(ConsumerListener listener : listeners){
			listener.onConsumeMessageError(context, e);
		}
	}


}
