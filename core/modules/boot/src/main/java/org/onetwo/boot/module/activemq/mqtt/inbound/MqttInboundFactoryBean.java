package org.onetwo.boot.module.activemq.mqtt.inbound;

import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.InBoundClientProps;
import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.MessageConverters;
import org.onetwo.boot.module.activemq.mqtt.JsonPahoMessageConverter;
import org.onetwo.boot.module.activemq.mqtt.MqttPahoMessageDrivenChannel;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * @author weishao zeng
 * <br/>
 */
public class MqttInboundFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	@Autowired
	private MqttPahoClientFactory clientFactory;
	@Autowired
	JsonPahoMessageConverter jsonPahoMessageConverter;
	private InBoundClientProps clientConfig;
	private Class<? extends MqttPahoMessageDrivenChannel> drivenChannelClass;
	
	private MqttPahoMessageDrivenChannel drivenChannel;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public Object getObject() throws Exception {
		MqttPahoMessageDrivenChannel drivenChannel = this.drivenChannel;
		
		if (drivenChannel==null) {
			drivenChannel = ReflectUtils.newInstance(drivenChannelClass, clientConfig, clientFactory);
			String channelName = clientConfig.getChannelName();
			if (StringUtils.isBlank(channelName)) {
				throw new BaseException("inbound output channel name can not blank!");
			}
			if (MessageConverters.JSON.equals(clientConfig.getConverter())) {
				JsonPahoMessageConverter converter = jsonPahoMessageConverter;
				drivenChannel.setConverter(converter);
			}
			drivenChannel.setOutputChannelName(channelName);
//			drivenChannel.setQos(this.clientConfig.getQos());
//			drivenChannel.setCompletionTimeout(clientConfig.getCompletionTimeout());
//			drivenChannel.afterPropertiesSet();
			SpringUtils.initializeBean(applicationContext, drivenChannel);
			
			this.drivenChannel = drivenChannel;
		}

		return drivenChannel;
	}

	@Override
	public Class<?> getObjectType() {
		return drivenChannelClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setClientConfig(InBoundClientProps clientConfig) {
		this.clientConfig = clientConfig;
	}

	public void setDrivenChannelClass(Class<? extends MqttPahoMessageDrivenChannel> drivenChannelClass) {
		this.drivenChannelClass = drivenChannelClass;
	}
	

}
