package org.onetwo.ext.ons;

import java.util.Collections;
import java.util.List;

import org.onetwo.ext.alimq.ProducerListener;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
public class ONSProducerListenerComposite implements InitializingBean, ProducerListener<Message> {
	private final Logger logger = ONSUtils.getONSLogger();

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
		if(logger.isInfoEnabled()){
			logger.info("send message topic: {}, tags: {}, key: {}", message.getTopic(), message.getTag(), message.getKey());
		}
		for(ProducerListener<Message> listener : listeners){
			listener.beforeSendMessage(message);
		}
	}
	
	@Override
	public void afterSendMessage(Message message, SendResult sendResult) {
		if(logger.isInfoEnabled()){
			logger.info("send message success. topic: {}, tags: {}, sendResult: {}", message.getTopic(), message.getTag(), sendResult);
		}
		for(ProducerListener<Message> listener : listeners){
			listener.afterSendMessage(message, sendResult);
		}
	}

	@Override
	public void onSendMessageError(Message message, Throwable throable) {
		if(logger.isErrorEnabled()){
			logger.error("send message topic: {}, tags: {}, key: {}", message.getTopic(), message.getTag(), message.getKey());
		}
		for(ProducerListener<Message> listener : listeners){
			listener.onSendMessageError(message, throable);
		}
	}


}
