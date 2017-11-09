package org.onetwo.ext.ons;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.ConsumerListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
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
		if(true){
			throw new ServiceException("error");
		}
		log.info("beforeConsumeMessage");
		for(ConsumerListener listener : listeners){
			listener.beforeConsumeMessage(context);
		}
	}

	@Override
	public void afterConsumeMessage(ConsumContext context) {
		log.info("afterConsumeMessage");
		for(ConsumerListener listener : listeners){
			listener.afterConsumeMessage(context);
		}
	}

	@Override
	public void onConsumeMessageError(ConsumContext context, Throwable e) {
		log.info("onConsumeMessageError");
		for(ConsumerListener listener : listeners){
			listener.onConsumeMessageError(context, e);
		}
	}


}
