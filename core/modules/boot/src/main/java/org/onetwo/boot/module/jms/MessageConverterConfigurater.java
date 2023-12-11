package org.onetwo.boot.module.jms;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

/***
 * 
		MessageHandlerMethodFactory handlerMethodFactory = this.registrar.getMessageHandlerMethodFactory();
 * @author way
 *
 */
public class MessageConverterConfigurater implements BeanPostProcessor {

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof JmsTemplate) {
			MappingJackson2MessageConverter jackson = new MappingJackson2MessageConverter();
			jackson.setEncoding(MappingJackson2MessageConverter.DEFAULT_ENCODING);
			JmsTemplate jmsTemplate = (JmsTemplate) bean;
//			jmsTemplate.setMessageConverter(jackson);
		} else if (bean instanceof MessageHandlerMethodFactory) {
		}
		return bean;
	}

}
