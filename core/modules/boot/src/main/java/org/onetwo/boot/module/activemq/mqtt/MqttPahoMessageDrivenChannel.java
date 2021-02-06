package org.onetwo.boot.module.activemq.mqtt;

import org.onetwo.boot.module.activemq.mqtt.ActiveMQTTProperties.InBoundClientProps;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

/**
 * @author weishao zeng
 * <br/>
 */

public class MqttPahoMessageDrivenChannel extends MqttPahoMessageDrivenChannelAdapter implements InitializingBean {
	
	private InBoundClientProps clientConfig;
	private ConfigurablePropertyAccessor wrapper;
	
	public MqttPahoMessageDrivenChannel(InBoundClientProps clientConfig, MqttPahoClientFactory clientFactory) {
		super(clientConfig.getClientId(), clientFactory, clientConfig.getTopics());
		this.clientConfig = clientConfig;
		this.wrapper = SpringUtils.newPropertyAccessor(this, true);
		if (StringUtils.isBlank(clientConfig.getChannelName())) {
			throw new BaseException("inbound output channel name can not blank!");
		}
		this.setOutputChannelName(clientConfig.getChannelName());
	}

	@Override
	public void onInit() {
		setQos(clientConfig.getQos());
		setCompletionTimeout(clientConfig.getCompletionTimeout());
		
		super.onInit();
	}
	
	public boolean isConnected() {
		try {
			boolean connected = (boolean)this.wrapper.getPropertyValue("connected");
			return connected;
		} catch (Exception e) {
			JFishLoggerFactory.getCommonLogger().error("check mqtt borker connect state error: " + e.getMessage(), e);
		}
		return false;
	}

}
