package org.onetwo.ext.ons;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.ext.alimq.ProducerListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class ONSProducerListenerComposite implements InitializingBean, ProducerListener<Message> {

	@Autowired(required=false)
	private List<ProducerListener<Message>> listeners;
	
	public ONSProducerListenerComposite() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.listeners = listeners==null?Collections.emptyList():listeners;
		AnnotationAwareOrderComparator.sort(listeners);
	}
	
	@Override
	public void beforeSendMessage(Message message) {
		log.info("beforeSendMessage: {}", message);
		for(ProducerListener<Message> listener : listeners){
			listener.beforeSendMessage(message);
		}
	}
	
	@Override
	public void onSendMessageError(Message message, Throwable throable) {
		log.info("onSendMessageError: {}", message);
		for(ProducerListener<Message> listener : listeners){
			listener.onSendMessageError(message, throable);
		}
	}


}
