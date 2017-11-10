package org.onetwo.ext.ons;

import java.util.Collections;
import java.util.List;

import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.ConsumerListener;
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
	public void beforeConsumeMessage(ConsumContext context) {
		for(ConsumerListener listener : listeners){
			listener.beforeConsumeMessage(context);
		}
	}

	@Override
	public void afterConsumeMessage(ConsumContext context) {
		for(ConsumerListener listener : listeners){
			listener.afterConsumeMessage(context);
		}
	}

	@Override
	public void onConsumeMessageError(ConsumContext context, Throwable e) {
		for(ConsumerListener listener : listeners){
			listener.onConsumeMessageError(context, e);
		}
	}


}
